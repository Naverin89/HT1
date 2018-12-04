package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBWorker {
	
	// Количество рядов таблицы, затронутых последним запросом.
	private Integer affected_rows = 0;
	
	// Значение автоинкрементируемого первичного ключа, полученное после
	// добавления новой записи.
	private Integer last_insert_id = 0;
	private Integer last_insert_phone_id = 0;

	// Указатель на экземпляр класса.
	private static DBWorker instance = null;
	
	String connectionItems = "jdbc:mysql://localhost/phonebook?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8&characterSetResults=utf8&connectionCollation=utf8_general_ci";
	Connection connect;
	
	// Метод для получения экземпляра класса (реализован Singleton).
	public static DBWorker getInstance()
	{
		if (instance == null)
		{
			instance = new DBWorker();
		}
	
		return instance;
	}
	
	// "Заглушка", чтобы экземпляр класса нельзя было получить напрямую.
	private DBWorker()
	{
	 // Просто "заглушка".			
	}
	
	// Выполнение подключения к базе данных.
	public void connectToDB() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();										
			connect = DriverManager.getConnection(connectionItems);
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e){
			{
				System.out.println("Can't perform connectToDB()!");
				e.printStackTrace();
			}
		}
		
	}
		
	// Выполнение запросов на выборку данных.
	public ResultSet getDBData(String query)
	{
		Statement statement;
		connectToDB();
		try
		{
			statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("null on getDBData()!");
		return null;

	}
	
	// Выполнение запросов на модификацию данных.
	public Integer changeDBData(String query)
	{
		Statement statement;
		connectToDB();
		try
		{
			statement = connect.createStatement();
			this.affected_rows = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		
			// Получаем last_insert_id() для операции вставки.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
            	this.last_insert_id = rs.getInt(1);
            }
			
			return this.affected_rows;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	// Добавление телефона в базу данных
	public Integer addPhoneNumberToDBData(String query) {
		connectToDB();
		try {
			Statement statement = connect.createStatement();
			this.affected_rows = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			// Получаем last_insert_id() для операции вставки.
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                this.last_insert_phone_id = rs.getInt(1);
            }
            return this.affected_rows;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("null on addPhoneNumberToDBData()!");
		return null;	
	}
	
	// Изменение телефона в базе данных.
	public Integer editPhoneInDBData(String query) {
		connectToDB();
		try {
			Statement statement = connect.createStatement();
			this.affected_rows= statement.executeUpdate(query);
			return this.affected_rows;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("null on editPhoneInDBData()!");
		return null;
	}
	
	// Удаление телефона из базы данных.
	public boolean deletePhoneInDBData(String query) {
		connectToDB();
		try {
			Statement statement = connect.createStatement();
			this.affected_rows= statement.executeUpdate(query);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("false in deletePhoneInDBData()!");
		return false;
	}
	
	
	// +++++++++++++++++++++++++++++++++++++++++++++++++
	// Геттеры и сеттеры.
	public Integer getAffectedRowsCount()
	{
		return this.affected_rows;
	}
	
	public Integer getLastInsertId()
	{
		return this.last_insert_id;
	}
	
	public Integer getLastInsertPhoneId()
	{
		return this.last_insert_phone_id;
	}
	
	// Геттеры и сеттеры.
	// -------------------------------------------------
}

