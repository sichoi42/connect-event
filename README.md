# 로컬 개발환경 설정

## 1. Server

```bash
cd connect-event-server
```

서버 디렉토리로 이동합니다.

```bash
./gradlew build -x test
```

프로젝트 빌드를 수행합니다.

```bash
java -jar build/libs/connectevent-server-0.0.1-SNAPSHOT.jar
```

서버를 실행합니다. (기본 프로필은 `local` 입니다.)

## 2. Client

```bash
cd connect-event-client
```

클라이언트 디렉토리로 이동합니다.

```bash
npm install
```

프로젝트 의존성을 설치합니다.

```bash
npm run dev
```

클라이언트를 실행합니다.

## 3. Infra

```bash
docker-compose up --build -d
```

데이터베이스 및 Nginx 컨테이너를 실행합니다.

## 4. 웹사이트 접속

http://localhost 으로 접속합니다.
