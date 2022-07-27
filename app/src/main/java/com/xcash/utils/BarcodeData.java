package com.xcash.utils;


import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class BarcodeData {

    public static final String XMR_SCHEME = "xcash:";
    public static final String XMR_PAYMENTID = "tx_payment_id";
    public static final String XMR_AMOUNT = "tx_amount";
    public static final String XMR_DESCRIPTION = "tx_description";

    public static final String OA_XMR_ASSET = "xcash";

    final public String address;
    final public String addressName;
    final public String paymentId;
    final public String amount;
    final public String description;

    public BarcodeData(String address) {
        this(address, null, null, null, null);
    }

    public BarcodeData(String address, String amount) {
        this(address, null, null, null, amount);
    }

    public BarcodeData(String address, String amount, String description) {
        this(address, null, null, description, amount);
    }

    public BarcodeData(String address, String paymentId, String description, String amount) {
        this(address, null, paymentId, description, amount);
    }

    public BarcodeData(String address, String addressName, String paymentId, String description, String amount) {
        this.address = address;
        this.addressName = addressName;
        this.paymentId = paymentId;
        this.description = description;
        this.amount = amount;
    }


    public Uri getUri() {
        return Uri.parse(getUriString());
    }

    public String getUriString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BarcodeData.XMR_SCHEME).append(address);
        boolean first = true;
        if ((paymentId != null) && !paymentId.isEmpty()) {
            sb.append("?");
            first = false;
            sb.append(BarcodeData.XMR_PAYMENTID).append('=').append(paymentId);
        }
        if ((description != null) && !description.isEmpty()) {
            sb.append(first ? "?" : "&");
            first = false;
            sb.append(BarcodeData.XMR_DESCRIPTION).append('=').append(Uri.encode(description));
        }
        if ((amount != null) && !amount.isEmpty()) {
            sb.append(first ? "?" : "&");
            sb.append(BarcodeData.XMR_AMOUNT).append('=').append(amount);
        }
        return sb.toString();
    }

    static public BarcodeData fromQrCode(String qrCode) {
        // check for monero uri
        BarcodeData bcData = parseMoneroUri(qrCode);
        // check for naked monero address / integrated address
        if (bcData == null) {
            bcData = parseMoneroNaked(qrCode);
        }

        return bcData;
    }

    /**
     * Parse and decode a monero scheme string. It is here because it needs to validate the data.
     *
     * @param uri String containing a monero URL
     * @return BarcodeData object or null if uri not valid
     */

    static public BarcodeData parseMoneroUri(String uri) {
        if (uri == null) return null;

        if (!uri.startsWith(XMR_SCHEME)) return null;

        String noScheme = uri.substring(XMR_SCHEME.length());
        Uri monero = Uri.parse(noScheme);
        Map<String, String> parms = new HashMap<>();
        String query = monero.getEncodedQuery();
        if (query != null) {
            String[] args = query.split("&");
            for (String arg : args) {
                String[] namevalue = arg.split("=");
                if (namevalue.length == 0) {
                    continue;
                }
                parms.put(Uri.decode(namevalue[0]).toLowerCase(),
                        namevalue.length > 1 ? Uri.decode(namevalue[1]) : "");
            }
        }
        String address = monero.getPath();

        String paymentId = parms.get(XMR_PAYMENTID);
        // deal with empty payment_id created by non-spec-conforming apps
        if ((paymentId != null) && paymentId.isEmpty()) paymentId = null;

        String description = parms.get(XMR_DESCRIPTION);
        String amount = parms.get(XMR_AMOUNT);
        if (amount != null) {
            try {
                Double.parseDouble(amount);
            } catch (NumberFormatException ex) {
                return null; // we have an amount but its not a number!
            }
        }
        if ((paymentId != null) && !StringTool.isPaymentIdValid(paymentId)) {
            //Timber.d("paymentId invalid");
            return null;
        }

        if (!StringTool.isAddressValid(address)) {
            //Timber.d("address invalid");
            return null;
        }
        return new BarcodeData(address, paymentId, description, amount);
    }

    static public BarcodeData parseMoneroNaked(String address) {
        if (address == null) return null;

        if (!StringTool.isAddressValid(address)) {
            //Timber.d("address invalid");
            return null;
        }

        return new BarcodeData(address);
    }
}