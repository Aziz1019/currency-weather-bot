package com.example.cbu.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString() {
        return  "O'zbek So'miga qiymati: " + rate + "\n" +
                "O'sish: " + diff + "\n" +
                "Sana: " + date;
    }
}
