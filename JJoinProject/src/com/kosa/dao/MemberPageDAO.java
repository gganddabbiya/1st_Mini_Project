package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kosa.db.DBConnection;
import com.kosa.dto.TaskDTO;

import oracle.jdbc.OracleTypes;

public class MemberPageDAO {
	private Connection connection = DBConnection.getConnection();

	public void updateTaskCompletion(int taskID, int completion) {
		try {
			CallableStatement statement = connection.prepareCall("{call member_page_pkg.update_task_completion(?, ?)}");
			statement.setInt(1, taskID);
			statement.setInt(2, completion);
			statement.executeUpdate();
			System.out.println("Task completion updated successfully.");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<TaskDTO> fetchMemberTasks(int memberId) { // member_id로 task 조회
		ArrayList<TaskDTO> tasks = new ArrayList<>();
		
		try {
			CallableStatement statement = connection.prepareCall("{call member_page_pkg.fetch_member_tasks(?, ?)}");
			statement.setInt(1, memberId);
			statement.registerOutParameter(2, OracleTypes.CURSOR); // OUT 매개변수를 등록하고 타입을 지정합니다.
			statement.execute();
			ResultSet resultSet = (ResultSet) statement.getObject(2); // OUT 매개변수로부터 결과 집합을 가져옵니다.
			
			while(resultSet.next()) {
				TaskDTO task = new TaskDTO(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getBoolean(4),
						resultSet.getInt(5), memberId, resultSet.getInt(7), resultSet.getBoolean(8));
				tasks.add(task);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	public void addTask(String taskName, String priority, int memberId, int projectId) {
		try {
			CallableStatement statement = connection.prepareCall("{call member_page_pkg.add_task(?, ?, ?, ?)}");
			statement.setString(1, taskName);
			statement.setString(2, priority);
			statement.setInt(3, memberId); // 메서드의 매개변수로 받은 memberId 사용
			statement.setInt(4, projectId); // 메서드의 매개변수로 받은 projectId 사용
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			System.err.println("Error occurred while calling stored procedure: " + e.getMessage());
			e.printStackTrace();
		}
	}

}