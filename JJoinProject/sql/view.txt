CREATE OR REPLACE VIEW member_task_view AS
SELECT task_id, task_name, priority, completion, project_project_id, project_member_member_id, feedback_total_score, retry, memo
FROM user11.task
ORDER BY CASE priority 
    WHEN 'High' THEN 1 
    WHEN 'Medium' THEN 2 
    WHEN 'Low' THEN 3 
    ELSE 4 
END;