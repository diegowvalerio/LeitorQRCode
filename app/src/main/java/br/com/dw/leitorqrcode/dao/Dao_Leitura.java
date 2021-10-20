package br.com.dw.leitorqrcode.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import br.com.dw.leitorqrcode.entidade.Leitura;

public class Dao_Leitura extends BaseDaoImpl<Leitura,Integer> {

    public Dao_Leitura(ConnectionSource connectionSource) throws SQLException {
        super(Leitura.class);
        setConnectionSource(connectionSource);
        initialize();
    }
}
