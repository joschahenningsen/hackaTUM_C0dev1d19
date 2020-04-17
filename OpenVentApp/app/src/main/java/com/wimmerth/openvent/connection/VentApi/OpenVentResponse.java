
package com.wimmerth.openvent.connection.VentApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenVentResponse {

    @SerializedName("0")
    @Expose
    private com.wimmerth.openvent.connection.VentApi._0 _0;

    public com.wimmerth.openvent.connection.VentApi._0 get0() {
        return _0;
    }

    public void set0(com.wimmerth.openvent.connection.VentApi._0 _0) {
        this._0 = _0;
    }

}
