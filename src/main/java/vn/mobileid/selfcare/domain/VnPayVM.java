package vn.mobileid.selfcare.domain;

import lombok.Data;

@Data
public class VnPayVM {
    private String ordertype;
    private int amount;
    private String vnpOrderInfo;
    private String bankcode;
    private String language;
}
