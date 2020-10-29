/*
 *
 *  (c)  Copyright 2020 Undercurrency
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.undercurrency.audiomoth.usbhid.events;

import java.util.Date;

import static com.undercurrency.audiomoth.usbhid.USBUtils.readIntFromLittleEndian;

public class AudioMothSetDateReceiveEvent {

    private Date date;
    public AudioMothSetDateReceiveEvent(byte[] buffer){
        int time = readIntFromLittleEndian(buffer,0);
        this.date = new Date();
        this.date.setTime(time);
    }

    public Date getDate(){
        return this.date;
    }
}