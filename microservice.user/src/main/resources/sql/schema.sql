CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
)//

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    nickname VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
)//

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
)//

-- ============================================================
-- STORED PROCEDURE 1: Registrar usuario con rol por defecto
-- ============================================================
CREATE OR REPLACE PROCEDURE sp_register_user(
    p_name VARCHAR,
    p_lastname VARCHAR,
    p_birthdate DATE,
    p_nickname VARCHAR,
    p_password VARCHAR,
    OUT p_user_id BIGINT
)
LANGUAGE plpgsql
AS $procedure$
DECLARE
    v_role_id BIGINT;
BEGIN
    INSERT INTO users (name, lastname, birthdate, nickname, password)
    VALUES (p_name, p_lastname, p_birthdate, p_nickname, p_password)
    RETURNING id INTO p_user_id;

    SELECT id INTO v_role_id FROM roles WHERE name = 'USER';

    IF v_role_id IS NOT NULL THEN
        INSERT INTO user_roles (user_id, role_id) VALUES (p_user_id, v_role_id);
    END IF;
END;
$procedure$//

-- ============================================================
-- STORED PROCEDURE 2: Obtener perfil completo del usuario
-- ============================================================
CREATE OR REPLACE FUNCTION sp_get_user_profile(p_nickname VARCHAR)
RETURNS TABLE(
    user_id BIGINT,
    user_name VARCHAR,
    user_lastname VARCHAR,
    user_birthdate DATE,
    user_nickname VARCHAR,
    role_names TEXT
)
LANGUAGE plpgsql
AS $procedure$
BEGIN
    RETURN QUERY
    SELECT
        u.id,
        u.name,
        u.lastname,
        u.birthdate,
        u.nickname,
        STRING_AGG(r.name, ',') AS role_names
    FROM users u
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN roles r ON ur.role_id = r.id
    WHERE u.nickname = p_nickname
    GROUP BY u.id, u.name, u.lastname, u.birthdate, u.nickname;
END;
$procedure$//
