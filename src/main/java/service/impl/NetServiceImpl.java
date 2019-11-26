package service.impl;

import controller.vo.Status;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.NetService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NetServiceImpl implements NetService {

    private final Sigar sigar;
    private Map<String, NetInterfaceStat> oldStat;
    private Map<String, NetInterfaceStat> newStat;
    private ReadWriteLock lock;

    @Autowired
    public NetServiceImpl(Sigar sigar) {
        this.sigar = sigar;
        lock = new ReentrantReadWriteLock();
        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::scheduled, 0, 1, TimeUnit.SECONDS);
    }

    private void scheduled(){
        try {
            lock.writeLock().lock();
            oldStat = newStat;
            newStat = Arrays
                    .stream(sigar.getNetInterfaceList())
                    .collect(Collectors.toMap(Function.identity(), s -> {
                        try {
                            return sigar.getNetInterfaceStat(s);
                        } catch (SigarException e) {
                            throw new RuntimeException(e);
                        }
                    }));
        } catch (SigarException e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<String> netInfo() {
        try {
            return Arrays
                    .stream(sigar.getNetInterfaceList())
                    .collect(Collectors.toList());
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Status> netStatus() {
        Date date = new Date();
        try {
            return List.of(
                    new Status()
                            .setTime(date)
                            .setStatus(Arrays.stream(sigar.getNetInterfaceList())
                            .collect(Collectors.toMap(Function.identity(), this::getUp))),
                    new Status()
                            .setTime(date)
                            .setStatus(Arrays.stream(sigar.getNetInterfaceList())
                            .collect(Collectors.toMap(Function.identity(), this::getDown)))
            );
        } catch (SigarException e) {
            throw new RuntimeException(e);
        }
    }

    private double getUp(String name) {
        try{
            lock.readLock().lock();
            return (newStat.get(name).getTxBytes() - oldStat.get(name).getTxBytes())/1024.0;
        } finally {
            lock.readLock().unlock();
        }

    }

    private double getDown(String name) {
        try{
            lock.readLock().lock();
            return (newStat.get(name).getRxBytes() - oldStat.get(name).getRxBytes())/1024.0;
        } finally {
            lock.readLock().unlock();
        }

    }

}
