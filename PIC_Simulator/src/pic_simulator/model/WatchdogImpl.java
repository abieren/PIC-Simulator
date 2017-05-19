/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.interfaces.Watchdog;
import pic_simulator.utils.BinaryNumberHelper;

/**
 *
 * @author Alex
 */
public class WatchdogImpl implements Watchdog {
    
    private final double WD_MIN_TRIGGER_TIME = 18; //milliseconds
    private double _timePast = 0;
    private double _timeToTrigger = 0;
    private boolean _watchdogEnabled = true;
    private boolean _postscalerAssigned = false;
    private int _postscalerRate = 1;
    private boolean _hasTriggered = false;
    
    private void updateTimeToTrigger() {
        if (!_postscalerAssigned) {
            _timeToTrigger = WD_MIN_TRIGGER_TIME;
        } else {
            _timeToTrigger = WD_MIN_TRIGGER_TIME * _postscalerRate;
        }
        clear();
    }

    public WatchdogImpl() {
        updateTimeToTrigger();
    }
    
    @Override
    public void enable() {
        _watchdogEnabled = true;
    }
    
    @Override
    public void disable() {
        _watchdogEnabled = false;
    }
    
    @Override
    public void clear() {
        _timePast = 0;
        _hasTriggered = false;
    }
    
    @Override
    public boolean hasTriggered() {
        return _hasTriggered;
    }
    
    @Override
    public void setPostscalerAssignment(boolean PSAbit) {
        _postscalerAssigned = PSAbit;
        updateTimeToTrigger();
    }

    @Override
    public void setPostscalerRate(boolean PS0bit, boolean PS1bit, boolean PS2bit) {
        int tmp = 0;
        tmp = BinaryNumberHelper.setBit(tmp, 0, PS0bit);
        tmp = BinaryNumberHelper.setBit(tmp, 1, PS1bit);
        tmp = BinaryNumberHelper.setBit(tmp, 2, PS2bit);
        _postscalerRate = (int)Math.pow(2, tmp);
        updateTimeToTrigger();
    }

    @Override
    public void notifyTimeAdvanced(double timeDeltaMs) {
        if (!_watchdogEnabled) return;
        _timePast = _timePast + timeDeltaMs;
        if (_timePast >= _timeToTrigger) {
            _hasTriggered = true;
        }
    }

}
