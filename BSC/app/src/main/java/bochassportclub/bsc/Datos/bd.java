package bochassportclub.bsc.Datos;

import android.os.StrictMode;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import bochassportclub.bsc.Entities.Barrio;
import bochassportclub.bsc.Entities.Domicilio;
import bochassportclub.bsc.Entities.Jugador;
import bochassportclub.bsc.Entities.Localidad;
import bochassportclub.bsc.Entities.Noticias;
import bochassportclub.bsc.Entities.Persona;
import bochassportclub.bsc.Entities.TipoDocumento;
import bochassportclub.bsc.Entities.Usuario;
import bochassportclub.bsc.R;

public class bd {

    public static Connection getConnection() throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
//        String url = "jdbc:jtds:sqlserver://sql5020.site4now.net;databaseName=DB_A3F6C9_BOCHAS;user=DB_A3F6C9_BOCHAS_admin;password=bochas2018";
        String url = "jdbc:jtds:sqlserver://sql7001.site4now.net;databaseName=DB_A410C5_dncortez14;user=DB_A410C5_dncortez14_admin;password=dncortez14";
        return DriverManager.getConnection(url);
    }

    public static List<Localidad> getLocalidades() {
        List<Localidad> list = new ArrayList<>();

        try {
            Connection connection = getConnection();
            String sql = "select id, nombre from Localidad";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Localidad l = new Localidad(rs.getInt("Id"), rs.getString("nombre"));
                list.add(l);
            }

            rs.close();
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<TipoDocumento> getTipoDocumento() {
        List<TipoDocumento> list = new ArrayList<>();

        try {
            Connection connection = getConnection();
            String sql = "select * from TipoDocumento";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                TipoDocumento td = new TipoDocumento();
                td.setId(rs.getInt("Id"));
                td.setNombre(rs.getString("Nombre"));
                list.add(td);
            }

            rs.close();
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean isValidUserName(String nombre_usuario) {
        boolean ret = true;
        try {
            Connection connection = getConnection();
            String sql = "select top 1 id from Usuario where Nombre = ? ";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nombre_usuario);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ret = false;
            }

            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public static boolean isValidDninumber(String nro) {
        boolean ret = true;
        try {
            Connection connection = getConnection();
            String sql = "select top 1 * from persona where NroDocumento = ? and tipo = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nro);
            pst.setString(2, "JUGADOR");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ret = false;
            }

            rs.close();
            pst.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public static List<Barrio> getBarrios(int idLocalidad) {
        List<Barrio> list = new ArrayList<>();

        try {
            Connection connection = getConnection();
            String sql = "select * from Barrio where IdLocalidad = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, idLocalidad);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Barrio b = new Barrio();
                b.setId(rs.getInt("Id"));
                b.setNombre(rs.getString("nombre"));
                b.setIdLocalidad(rs.getInt("IdLocalidad"));
                list.add(b);
            }

            rs.close();
            pst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int iniciarSesion(String user, String pwd) {
        int idJugador = 0;
        try {
            Connection connection = getConnection();
            String sql = "SELECT top 1 Usuario.Id \n" +
                    " FROM Usuario INNER JOIN\n" +
                    " Persona ON Usuario.Id = Persona.Id_Usuario INNER JOIN\n" +
                    " Jugador ON Persona.Id = Jugador.IdPersona\n" +
                    " WHERE  (Usuario.Nombre = ?) AND (Usuario.Contraseña = ?) AND (Persona.Fecha_Baja IS NULL) AND (Jugador.IdTipoJugador = 2)";
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, user);
            pst.setString(2, pwd);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idJugador = rs.getInt("Id");
            }

            pst.close();
            rs.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return idJugador;
    }

    public static boolean cerrarSesion(int idUsuario) {
        boolean ret;

        try {
            Connection connection = getConnection();
            CallableStatement cst = connection.prepareCall("call cerrarSesion(?)");
            cst.setInt(1, idUsuario);
            cst.execute();

            cst.close();
            connection.close();

            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public static boolean registrarSession(int idJugador) {
        boolean ret = false;
        try {
            int idSession = 0;
            Connection connection = getConnection();
            CallableStatement cst = connection.prepareCall("call iniciarSesion(?,?,?)");

            cst.registerOutParameter(1, Types.INTEGER);

            cst.setInt(2, idJugador);
            cst.setInt(3, 2);

            cst.execute();

            idSession = cst.getInt(1);

            if (idSession != 0) {
                ret = true;
            }

            cst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static int registrarJugador(Domicilio d, Usuario u, Persona p) {
        int idUsuario = 0;

        try {
            Connection connection = getConnection();
            CallableStatement cst = connection.prepareCall("{call insertarJugador(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            cst.registerOutParameter(1, Types.INTEGER);//idPersona

            // Domicilio
            cst.setInt(2, d.getIdBarrio());
            cst.setInt(3, d.getNumero());
            cst.setString(4, d.getCalle());
            cst.setInt(5, d.getIdLocalidad());
            cst.setString(6, d.getDepartamento());
            cst.setInt(7, d.getPiso());

            // Usuario
            cst.setString(8, u.getNombre());
            cst.setString(9, u.getContrasenia());

            // Persona
            cst.setString(10, p.getNroDocumento());
            cst.setInt(11, p.getIdTipoDocumento());
            cst.setString(12, p.getNombre());
            cst.setString(13, p.getApellido());
            cst.setString(14, p.getMail());
            cst.setString(15, p.getTelefono());
            cst.setString(16, p.getTipo());

            // Jugador
            cst.setInt(17, 2);

            cst.execute();

            idUsuario = cst.getInt(1);

            cst.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return idUsuario;
    }
}