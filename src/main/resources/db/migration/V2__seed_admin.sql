-- V2__seed_admin.sql
-- Usuario admin de prueba (password: Admin1234!)
-- Hash BCrypt generado con strength=10

INSERT INTO users (email, password, first_name, last_name, role, enabled)
VALUES (
    'admin@portfolio.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'Admin',
    'Sistema',
    'ROLE_ADMIN',
    TRUE
) ON CONFLICT (email) DO NOTHING;
