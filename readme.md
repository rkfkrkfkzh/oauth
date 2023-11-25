# OAuth2.0

이 프로젝트는 Spring Boot와 OAuth2.0을 활용하여 개발되었습니다.

## 시작하기

로컬 개발 및 테스트 목적으로 프로젝트의 실행할 수 있습니다.

### 선행 조건

이 프로젝트를 설치하고 실행하기 전에 다음 소프트웨어가 필요합니다:

- JDK 17
- Gradle
- MySQL
- spring boot 2.7.9

### 설치하기

MySQL에서 사용자를 생성하고 권한을 부여하는 방법은 다음과 같습니다.

```bash
mysql -u root -p  
CREATE USER 'new_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON my_new_database.* TO 'new_user'@'localhost';
FLUSH PRIVILEGES;
