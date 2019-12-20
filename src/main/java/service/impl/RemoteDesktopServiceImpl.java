package service.impl;

import org.springframework.stereotype.Service;
import service.RemoteDesktopService;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class RemoteDesktopServiceImpl implements RemoteDesktopService {

    private Robot robot;
    private Rectangle screenRectangle;

    public RemoteDesktopServiceImpl() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    @Override
    public BufferedImage screenshots() {
        return robot.createScreenCapture(screenRectangle);
    }
}
