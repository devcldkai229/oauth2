IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'accounts')
BEGIN
    CREATE TABLE accounts (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(100) NOT NULL,
        password VARCHAR(200) NOT NULL,
        email VARCHAR(200) NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'invalidated_token')
BEGIN
    CREATE TABLE invalidated_token (
        jwtId VARCHAR(255) PRIMARY KEY
    );
END;


IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'refresh_token')
BEGIN
    CREATE TABLE refresh_token (
        jwt_id VARCHAR(255) PRIMARY KEY,
        user_id INT NOT NULL,
        token VARCHAR(MAX) NOT NULL,
        expiry_time TIMESTAMP NOT NULL,
        created_at TIMESTAMP DEFAULT NOW(),
        revoked BOOLEAN DEFAULT FALSE,
        CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES accounts(id) ON DELETE CASCADE
    );
END;




