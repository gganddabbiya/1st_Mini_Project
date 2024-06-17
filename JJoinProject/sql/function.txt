create or replace FUNCTION calculate_average_advice_score (
    i_task_id IN NUMBER,
    i_advice_score IN NUMBER
)
RETURN NUMBER
IS
    v_num_members NUMBER;
    v_sum NUMBER;
    n_sum NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_num_members
    FROM user11.feedback
    WHERE TASK_task_id = i_task_id;
    
    DBMS_OUTPUT.PUT_LINE(v_num_members);
    
    SELECT SUM(feedback_total_score) INTO v_sum
    FROM user11.TASK
    WHERE task_id = i_task_id;
    
    DBMS_OUTPUT.PUT_LINE(v_sum);

    IF v_sum IS NULL THEN
        n_sum := i_advice_score;
    ELSE
        n_sum := (v_sum * (v_num_members - 1) + i_advice_score) / v_num_members;
    END IF;

    RETURN n_sum;
END;
/
