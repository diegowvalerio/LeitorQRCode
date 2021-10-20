package br.com.dw.leitorqrcode.entidade;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tbleitura")
public class Leitura {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String leitura;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date data_leitura;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeitura() {
        return leitura;
    }

    public void setLeitura(String leitura) {
        this.leitura = leitura;
    }

    public Date getData_leitura() {
        return data_leitura;
    }

    public void setData_leitura(Date data_leitura) {
        this.data_leitura = data_leitura;
    }
}
