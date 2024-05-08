package com.kosa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberPageDAO {
    private Connection connection;

    public MemberPageDAO() {
        // DBConnection 클래스에서 새로운 Connection 객체를 가져옵니다.
        this.connection = DBConnection.getConnection();
    }

    public void fetchMemberTasks(int memberId) {
        try {
            CallableStatement statement = connection.prepareCall("{call fetch_member_tasks(?)}");
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String taskName = resultSet.getString("task_name");
                String priority = resultSet.getString("priority");

                // 여기에서 UI로 데이터를 전달하는 방법에 대해 생각해보세요.
                // UI에 정보를 전달하기 위한 새로운 방법을 사용할 수 있습니다.
                // JTextArea에 추가하거나 JTable에 표시하는 대신에
                // UI 클래스에 메서드를 추가하여 데이터를 전달하는 것도 고려할 수 있습니다.
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTaskCompletion(int taskId, int completion) {
        try {
            CallableStatement statement = connection.prepareCall("{call update_task_completion(?, ?)}");
            statement.setInt(1, taskId);
            statement.setInt(2, completion);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTask(String taskName, String priority, int memberId, int projectId) {
        try {
            CallableStatement statement = connection.prepareCall("{call add_task(?, ?, ?, ?)}");
            statement.setString(1, taskName);
            statement.setString(2, priority);
            statement.setInt(3, memberId);
            statement.setInt(4, projectId);
            statement.executeUpdate();
            connection.commit(); // 커밋 추가
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     
}
