package jx.devices;

import jx.zero.Naming;

public interface DeviceFinder {
    public Device[] find(String[] args, Naming naming);
}
