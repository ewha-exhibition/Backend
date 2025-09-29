# trouble shooting
1. Preflight Request (사전 요청)란?
- Preflight 요청은 브라우저가 **본 요청(예: GET, POST)을 보내기 전에, 이 요청을 보내는 것이 안전한지 서버에게 미리 확인받는 '사전 허가 요청'**입니다. 
- 이 사전 요청을 보낼 때 사용하는 HTTP 메소드가 바로 **OPTIONS**입니다.

2. 동작 흐름
- 동작 흐름 
- [프론트엔드 → 백엔드] Preflight 요청 전송
  - 메소드: OPTIONS
  - 목적지: /api/me
  - 내용 (헤더): "안녕하세요, http://localhost:3000에서 왔습니다. 잠시 후에 GET 메소드와 Authorization 헤더를 담은 진짜 요청을 보내려고 하는데, 허락해 주실 수 있나요?"

- [백엔드 → 프론트엔드] Preflight 응답
  - 
  - 역할 담당: SecurityConfig에 통합된 CORS 설정 (CorsConfigurationSource)
  - 내용: "네, http://localhost:3000에서 오는 요청은 허락된 방문객입니다. GET 메소드와 Authorization 헤더 사용도 괜찮습니다." 라는 의미의 응답 헤더를 보냅니다. (Access-Control-Allow-Origin, Access-Control-Allow-Methods 등)

[프론트엔드 → 백엔드] 본 요청 전송

메소드: GET

목적지: /api/me

내용 (헤더): Authorization: Bearer [JWT 토큰]

설명: Preflight 요청이 성공적으로 끝나면, 브라우저는 이제 안심하고 원래 보내려던 본 요청을 보냅니다.

[백엔드 → 프론트엔드] 본 요청 처리 및 응답

역할 담당: JwtAuthenticationFilter, UserService, UserController

설명: 백엔드는 JWT를 검증하고, 사용자 정보를 조회하여 최종 데이터를 응답합니다.