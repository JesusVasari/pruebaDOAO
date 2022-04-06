import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.text.SimpleDateFormat



fun main() {

    val c = ConnectionBuilder()
    println("conectando.....")

    if (c.connection.isValid(10)) {
        println("Conexión válida")

        c.connection.use {
            val h2DAO = UserDAO(c.connection)

            // Creamos la tabla o la vaciamos si ya existe
            h2DAO.prepareTable()

            // Insertamos 4 usuarios
            repeat(1)
            {


                h2DAO.insert(Libros(101,"Gambardella","XML developed Guide","computer",44.95,"An in-depth look at creating applications with XML"))
                h2DAO.insert(Libros(102,"Ralls"," Midnight Rain","Fantasia",5.95,"A former architect battles corporate zombies, an evil sorceress, and her own childhood to become queen of the world"))
                h2DAO.insert(Libros(103,"Corets","Meave Ascendant","Fantasia",5.95,"After the collapse of a nanotechnology society in England, the young survivors lay the foundation for a new society"))
                h2DAO.insert(Libros(104,"Corets","Iberons Legacy","Fantasia",5.95,"In post-apocalypse England, the mysterious nagent known only as Oberon helps to create a new life for the inhabitants of London. Sequel to Maeve Ascendant"))
                h2DAO.insert(Libros(105,"Corets","the surrender bird","Fntasia",5.95,"The two daughters of Maeve, half-sisters, battle one another for control of England. Sequel to Oberon''s Legacy"))

                h2DAO.insert(Libros(106,"Rondall","Lover Birds","Romance",4.95,"when Carla meets Paul at an ornithology conference, tempers fly as feathers get ruffled"))
                h2DAO.insert(Libros(107,"Thurman","Spilsh Splash","Romance",4.95,"A deep sea diver finds true love twenty thousand leagues beneath the sea"))
                h2DAO.insert(Libros(108,"Knor","Creepy Crawles","terror",4.95,"An anthology of horror stories about roaches, centipedes, scorpions  and other insects"))
                h2DAO.insert(Libros(109,"Kress","Paradox Lost","Ciencia ficcion",6.95,"After an inadvertant trip through a Heisenberg Uncertainty Device, James Salway discovers the problems of being quantum."))
                h2DAO.insert(Libros(110,"obrien","Microsoft NET","Computer",36.95,"Microsoft''s .NET initiative is explored in detail in this deep programmer''s reference"))
                h2DAO.insert(Libros(111,"Obrien","MSXML3: A Comprehensive Guide","Computer",36.95,"The Microsoft MSXML3 parser is covered in detail, with attention to XML DOM interfaces, XSLT processing, SAX and more"))
                h2DAO.insert(Libros(112,"Obrien","Visual Studio 7","Computer",49.95,"Microsoft Visual Studio 7 is explored in depth, looking at how Visual Basic, Visual C++, C#, and ASP+ are integrated into a comprehensive development environment"))



            }  // Bus car un usuario
            val u = h2DAO.selectUser(103)
            println(u)
            // Actualizar un usuario
            if (u!=null)
            {
                u.autor = "nuevo usuario"
                h2DAO.updateUser(u)
            }

            // Borrar un usuario
            h2DAO.deleteUser(110)

            // Seleccionar todos los usuarios
            println(message = h2DAO.selectAllUsers())
        }
    } else
        println("Conexión ERROR")
}
class ConnectionBuilder {
    lateinit var connection: Connection
    private val jdbcURL = "jdbc:oracle:thin:@localhost:1521:XE"
    private val jdbcUsername = "alumno"
    private val jdbcPassword = "alumno"

    init {
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)
            connection.autoCommit = false
        } catch (e: SQLException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

}








