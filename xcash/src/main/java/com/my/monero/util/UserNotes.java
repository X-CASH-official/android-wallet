/**
 * Copyright (c) 2017-2018 m2049r
 * <p>
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */

package com.my.monero.util;


import com.my.monero.api.QueryOrderStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserNotes {

    public String txNotes = "";
    public String note = "";
    public String xmrtoKey = null;
    public String xmrtoAmount = null; // could be a double - but we are not doing any calculations
    public String xmrtoDestination = null;

    public UserNotes(final String txNotes) {
        if (txNotes == null) {
            return;
        }
        this.txNotes = txNotes;
        Pattern p = Pattern.compile("^\\{(xmrto-\\w{6}),([0-9.]*)BTC,(\\w*)\\} ?(.*)");
        Matcher m = p.matcher(txNotes);
        if (m.find()) {
            xmrtoKey = m.group(1);
            xmrtoAmount = m.group(2);
            xmrtoDestination = m.group(3);
            note = m.group(4);
        } else {
            note = txNotes;
        }
    }

    public void setNote(String newNote) {
        if (newNote != null) {
            note = newNote;
        } else {
            note = "";
        }
        txNotes = buildTxNote();
    }

    public void setXmrtoStatus(QueryOrderStatus xmrtoStatus) {
        if (xmrtoStatus != null) {
            xmrtoKey = xmrtoStatus.getUuid();
            xmrtoAmount = String.valueOf(xmrtoStatus.getBtcAmount());
            xmrtoDestination = xmrtoStatus.getBtcDestAddress();
        } else {
            xmrtoKey = null;
            xmrtoAmount = null;
            xmrtoDestination = null;
        }
        txNotes = buildTxNote();
    }

    private String buildTxNote() {
        StringBuffer sb = new StringBuffer();
        if (xmrtoKey != null) {
            if ((xmrtoAmount == null) || (xmrtoDestination == null))
                throw new IllegalArgumentException("Broken notes");
            sb.append("{");
            sb.append(xmrtoKey);
            sb.append(",");
            sb.append(xmrtoAmount);
            sb.append("BTC,");
            sb.append(xmrtoDestination);
            sb.append("}");
            if ((note != null) && (!note.isEmpty()))
                sb.append(" ");
        }
        sb.append(note);
        return sb.toString();
    }

}