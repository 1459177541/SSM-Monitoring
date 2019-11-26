package service;

import controller.vo.Status;

import java.util.List;

public interface NetService {

    public List<String> netInfo();

    public List<Status> netStatus();

}
