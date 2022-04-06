import java.sql.Connection
import java.sql.SQLException

class UserDAO(private val c: Connection) {

    companion object {
        private const val SCHEMA = "ALUMNO"
        private const val TABLE = "LIBROS"
        private const val TRUNCATE_TABLE_LIBROS_SQL = "TRUNCATE TABLE LIBROS"
        private const val CREATE_TABLE_LIBROS_SQL =
         "CREATE TABLE LIBROS (id NUMERIC(3) NOT NULL ,autor varchar(120) NOT NULL, titulo varchar(220) NOT NULL, genero varchar(100), precio number(4), descr varchar(200),PRIMARY KEY (id) )"
        private const val INSERT_LIBROS_SQL = "INSERT INTO LIBROS (id,autor,titulo,genero,precio,descr) VALUES (?,?,?,?,?,?)"
        private const val SELECT_LIBROS_BY_ID ="select * from LIBROS where id=?"
        private const val SELECT_ALL_LIBROS = "select * from LIBROS"
        private const val DELETE_LIBROS_SQL = "delete from LIBROS where id = ?"
        private const val UPDATE_LIBROS_SQL = "update LIBROS set  autor = ? ,titulo= ? where id = ?"
    }


    fun prepareTable() {
        val metaData = c.metaData
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)
        if (!rs.next()) createTable() else truncateTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_LIBROS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_LIBROS_SQL)
            }
            //Commit the change to the database
            c.commit()

        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_LIBROS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_LIBROS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(user: Libros) {
        println(INSERT_LIBROS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_LIBROS_SQL).use { st ->
                st.setInt(1, user.id)
                st.setString(2, user.autor)
                st.setString(3, user.titulo)
                st.setString(4,user.genero)
                st.setDouble(5,user.precio)
                st.setString(6,user.descr)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectUser(id: Int): Libros? {
        println(SELECT_LIBROS_BY_ID)

        var user: Libros? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_LIBROS_BY_ID).use { st ->
                st.setInt(1, id)


                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val id = rs.getInt("id")
                    val name = rs.getString("autor")
                    val titulo = rs.getString("titulo")
                    val genero = rs.getString("genero")
                    val precio = rs.getDouble("precio")
                    val desc = rs.getString("descr")
                    user = Libros(id, name, titulo,genero,precio,desc)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return user
    }

    fun selectAllUsers(): List<Libros> {
        println(SELECT_ALL_LIBROS)
        // using try-with-resources to avoid closing resources (boiler plate code)
        var users: MutableList<Libros> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_LIBROS).use { st ->
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    var id = rs.getInt("id")
                    var name = rs.getString("autor")
                    var titulo = rs.getString("titulo")
                    var genero = rs.getString("genero")
                    var precio = rs.getDouble("precio")
                    var desc = rs.getString("descr")

                    users.add(Libros(id, name, titulo, genero,precio,desc))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return users
    }

    fun deleteUser(id: Int): Boolean {
        println(DELETE_LIBROS_SQL)

        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_LIBROS_SQL).use { st ->
                st.setInt(1, id)
                rowDeleted = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowDeleted
    }

    fun updateUser(user: Libros): Boolean {
        println(UPDATE_LIBROS_SQL)

        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_LIBROS_SQL).use { st ->
                st.setString(1, user.autor)
                st.setString(2, user.titulo)
                st.setInt(3, user.id)

                rowUpdated = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowUpdated
    }

    private fun printSQLException(ex: SQLException) {
        for (e in ex) {
            if (e is SQLException) {
                e.printStackTrace(System.err)
                System.err.println("SQLState: " + e.sqlState)
                System.err.println("Error Code: " + e.errorCode)
                System.err.println("Message: " + e.message)
                var t = ex.cause
                while (t != null) {
                    println("Cause: $t")
                    t = t.cause
                }
            }
        }
    }

}