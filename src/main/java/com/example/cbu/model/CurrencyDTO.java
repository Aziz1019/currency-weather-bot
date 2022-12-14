package com.example.cbu.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyDTO {
    private String id;
    @JsonProperty(value = "Code")
    private String code;
    @JsonProperty(value = "Ccy")
    private String ccy;
    @JsonProperty(value = "CcyNm_EN")
    private String ccyNm_EN;
    @JsonProperty(value = "Nominal")
    private String nominal;
    @JsonProperty("Rate")
    private String rate;
    @JsonProperty("Diff")
    private String diff;
    @JsonProperty("Date")
    private String date;


    public CurrencyDTO() {
    }

    public CurrencyDTO(String id, String code, String ccy, String ccyNm_EN, String nominal, String rate, String diff, String date) {
        this.id = id;
        this.code = code;
        this.ccy = ccy;
        this.ccyNm_EN = ccyNm_EN;
        this.nominal = nominal;
        this.rate = rate;
        this.diff = diff;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getCcyNm_EN() {
        return ccyNm_EN;
    }

    public void setCcyNm_EN(String ccyNm_EN) {
        this.ccyNm_EN = ccyNm_EN;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return  "O'zbek So'miga qiymati: " + rate + "\n" +
                "O'sish: " + diff + "\n" +
                "Sana: " + date;
    }
}
