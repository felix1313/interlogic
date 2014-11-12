package com.felix.interlogic.game.db.dao;

import java.sql.SQLException;
import java.util.List;

import com.felix.interlogic.game.db.model.User;

public interface UserDao {
	public void addUser(User user) throws SQLException;

	public void updateUser(User user) throws SQLException;

	public void deleteUser(User user) throws SQLException;

	public List<User> getAllUsers() throws SQLException;

	public User getUserById(int userId) throws SQLException;

	public User getUserByLogin(String userLogin) throws SQLException;
}
