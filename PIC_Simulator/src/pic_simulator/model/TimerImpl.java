/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic_simulator.model;

import pic_simulator.interfaces.Notifier;
import pic_simulator.interfaces.Timer;
import pic_simulator.utils.BinaryNumberHelper;

/**
 *
 * @author Alex
 */
public class TimerImpl implements Timer {
    
    private final int PRESCALER_RATE_WITHOUT_PRESCALER_ASSIGNED = 1;
    private final int TIMER_OVERFLOW_LIMIT = 0xFF;
    
    private boolean _isWorkingAsTimer = true;
    private boolean _incrementOnFallingEdge = true;
    private boolean _isPrescalerAssigned = false;
    private int _prescalerRate = 2;
    private int _prescalerCount = 0;
    private int _timerCount = 0;
    private boolean _hasTriggered = false;
    private Notifier _notifier = null;
    
    private void incrementTimerCount() {
        int oldValue = _timerCount;
        _timerCount++;
        int overflow = _timerCount / TIMER_OVERFLOW_LIMIT;
        _timerCount = _timerCount & TIMER_OVERFLOW_LIMIT;
        if (overflow > 0) {
            _hasTriggered = true;
        }
        _notifier.changedRegister(PICSimulator.TMR0_REGISTER_BANK0, oldValue, _timerCount);
    }
    
    private void incrementPrescalerCount() {
        _prescalerCount++;
        int overflow;
        if (_isPrescalerAssigned) {
            overflow = _prescalerCount/_prescalerRate;
            _prescalerCount = _prescalerCount % _prescalerRate;
        } else {
            overflow = _prescalerCount/PRESCALER_RATE_WITHOUT_PRESCALER_ASSIGNED;
            _prescalerCount = _prescalerCount % _prescalerRate;
        }
        if (overflow > 0) {
            incrementTimerCount();
        }
    }

    public TimerImpl(Notifier notifier) {
        _notifier = notifier;
    }
    
    @Override
    public void clear() {
        _prescalerCount = 0;
        _timerCount = 0;
    }

    @Override
    public boolean hasTriggered() {
        return _hasTriggered;
    }

    @Override
    public void setPrescalerAssignment(boolean PSAbit) {
        _isPrescalerAssigned = !PSAbit;
        clear();
    }

    @Override
    public void setPrescalerRate(boolean PS0bit, boolean PS1bit, boolean PS2bit) {
        int tmp = 0;
        tmp = BinaryNumberHelper.setBit(tmp, 0, PS0bit);
        tmp = BinaryNumberHelper.setBit(tmp, 1, PS1bit);
        tmp = BinaryNumberHelper.setBit(tmp, 2, PS2bit);
        _prescalerRate = (int)Math.pow(2, tmp)*2;
        clear();
    }

    @Override
    public void setWorkingMode(boolean isWorkingAsTimer) {
        _isWorkingAsTimer = isWorkingAsTimer;
    }

    @Override
    public void setIncrementingEdgeMode(boolean incrementOnFallingEdge) {
        _incrementOnFallingEdge = incrementOnFallingEdge;
    }

    @Override
    public void notifyEdge(boolean isFallingEdge) {
        //check if timer works as counter
        if (!_isWorkingAsTimer) {
            if ( (_incrementOnFallingEdge && isFallingEdge) ||
                    (!_incrementOnFallingEdge) && (!isFallingEdge)) {
                incrementPrescalerCount();
            }
        }
    }

    @Override
    public void notifyCycle() {
        //check if timer works as timer
        if (_isWorkingAsTimer) {
            incrementPrescalerCount();
        }
    }

    @Override
    public void setTimerCount(int value) {
        value = BinaryNumberHelper.truncateToNBit(value, 8);
        _timerCount = value;
        //clear(); this will overrite _timerCount ....
    }

    @Override
    public int getTimerCount() {
        return _timerCount;
    }
    
}
