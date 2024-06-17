package com.kosa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kosa.db.DBConnection;
import com.kosa.dto.TaskDTO;

import oracle.jdbc.OracleTypes;

public class TaskDAO {
	private Connection conn = DBConnection.getConnection();
	private String runSP;
	private CallableStatement callableStatement;

	public ArrayList<TaskDTO> getTasksByProjectId(int projectId) {
		ArrayList<TaskDTO> tasks = new ArrayList<>();

		runSP = "{ call task_pack.get_tasks_by_project_id(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, projectId);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

			try {
				callableStatement.executeQuery();
				ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

				while (resultSet.next()) {
					TaskDTO task = new TaskDTO(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
							resultSet.getBoolean(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getInt(7),
							resultSet.getBoolean(8));
					tasks.add(task);
				}

			} catch (SQLException e) {
				System.out.println("프로시저에서 에러 발생!");

				System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasks;
	}

	public int getTaskCountByMemberId(int memberId) {
		int taskCount = 0;

		runSP = "{ call task_pack.get_task_count_by_member_id(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, memberId);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);

			callableStatement.executeQuery();

			taskCount = callableStatement.getInt(2);

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskCount;
	}

	public int getCompleteTaskCountByMemberId(int memberId) {
		int completeTaskCount = 0;

		runSP = "{ call task_pack.get_complete_task_count_by_member_id(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, memberId);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);

			callableStatement.executeQuery();

			completeTaskCount = callableStatement.getInt(2);

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return completeTaskCount;
	}

	public int getTotalTaskCountByProjectId(int projectId) {
		int taskCount = 0;

		runSP = "{ call task_pack.get_total_task_count_by_project_id(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, projectId);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);

			callableStatement.executeQuery();

			taskCount = callableStatement.getInt(2);

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskCount;
	}

	public int getTotalCompleteTaskCountByProjectId(int projectId) {
		int completeTaskCount = 0;

		runSP = "{ call task_pack.get_total_complete_task_count_by_project_id(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, projectId);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);

			callableStatement.executeQuery();

			completeTaskCount = callableStatement.getInt(2);

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return completeTaskCount;
	}

	public void addFeedback(String adviceName, int adviceScore, int taskID, int memberID, int memberID2, int projectID)
			throws SQLException {
		try {
			// 프로시저 호출
			System.out.println(1);
			callableStatement = conn.prepareCall("{call task_page_pkg.add_feedback(?, ?, ?, ?, ?, ?)}");
			System.out.println(2);
			callableStatement.setString(1, adviceName);
			callableStatement.setInt(2, adviceScore);
			callableStatement.setInt(3, taskID);
			callableStatement.setInt(4, memberID);
			callableStatement.setInt(5, memberID2);
			callableStatement.setInt(6, projectID);
			System.out.println(3);

			// 프로시저 실행

			callableStatement.execute();
		} catch (SQLException ex) {
			// 에러 처리
			System.out.println(4);
			ex.printStackTrace();
			throw ex;
		}
	}

	public void deleteFeedback(int taskID, int memberID, int projectID) throws SQLException {
		try {
			callableStatement = conn.prepareCall("{call task_page_pkg.delete_feedback(?, ?, ?)}");
			callableStatement.setInt(1, taskID);
			callableStatement.setInt(2, memberID);
			callableStatement.setInt(3, projectID);
			callableStatement.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public String getSelectedFeedback(int taskID, int memberID, int projectID) throws SQLException {
		String selectedFeedback = null;
		try {
			callableStatement = conn.prepareCall("{call task_page_pkg.get_feedback(?, ?, ?, ?)}");
			callableStatement.setInt(1, taskID);
			callableStatement.setInt(2, memberID);
			callableStatement.setInt(3, projectID);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR); // OUT 파라미터

			// 프로시저 실행
			callableStatement.execute();
			// OUT 파라미터 값 가져오기
			selectedFeedback = callableStatement.getString(4);
		} catch (SQLException e) {
			if (e.getErrorCode() == 1403) {
				// 데이터를 찾을 수 없는 경우
				System.out.println("해당 데이터를 찾을 수 없습니다.");
			} else {
				// 그 외의 경우 예외 처리
				e.printStackTrace(); // 에러 처리
			}
		}
		return selectedFeedback;
	}

	public void updateFeedback(int taskID, int memberID, int projectID, String adviceName, int adviceScore)
			throws SQLException {
		try {
			// 프로시저 호출
			callableStatement = conn.prepareCall("{call task_page_pkg.update_feedback(?, ?, ?, ?, ?)}");
			callableStatement.setInt(1, taskID);
			callableStatement.setInt(2, memberID);
			callableStatement.setInt(3, projectID);
			callableStatement.setString(4, adviceName);
			callableStatement.setInt(5, adviceScore);
			// connection.commit();

			// 프로시저 실행
			callableStatement.execute();
		} catch (SQLException ex) {
			// 에러 처리
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public void updateFeedbackTotalScore(int taskId, int advice_score) {
		runSP = "{ call task_page_pkg.update_feedback_total_score(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, taskId);
			callableStatement.setInt(2, advice_score);

			callableStatement.executeUpdate();
			
			System.out.println("feedback_total_score 업데이트 완료");

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateRetryTask(int taskId, int feedbackTotalScore) {
		runSP = "{ call task_page_pkg.update_task_retry(?, ?) }";

		try {
			callableStatement = conn.prepareCall(runSP);

			callableStatement.setInt(1, taskId);
			callableStatement.setInt(2, feedbackTotalScore);

			callableStatement.executeUpdate();
			
			System.out.println("retry 업데이트 완료");

		} catch (SQLException e) {
			System.out.println("프로시저에서 에러 발생!");

			System.err.format("SQL State: %s\\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
