
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/controle_estoque";

    private static final String USUARIO = "root";

    public static Connection conectar() throws SQLException {

        String senha = System.getenv("MYSQL_SENHA");

        if (senha == null) {
            throw new SQLException(
                    "A variável MYSQL_SENHA não foi configurada.");
        }

        return DriverManager.getConnection(
                URL, USUARIO, senha);
    }
}
