package com.example.cbu.model;
import com.example.cbu.entity.Currency;

public class CurrencyDTO {
    private String id;
    private String code;
    private String ccy;
    private String ccyNm_EN;
    private String nominal;
    private String rate;
    private String diff;
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

    public static Currency toEntity(CurrencyDTO currency) {
        return new Currency(
                Integer.parseInt(currency.getId()),
                Integer.parseInt(currency.getCode()),
                currency.getCcy(),
                currency.getCcyNm_EN(),
                Integer.parseInt(currency.getNominal()),
                Double.parseDouble(currency.getRate()),
                Double.parseDouble(currency.getDiff()),
                currency.getDate()
        );
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
