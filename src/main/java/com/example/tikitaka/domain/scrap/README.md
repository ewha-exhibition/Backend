
- Page와 Pageable의 역할 정리

| 역할                                 | 클래스          | 설명                                        |
| ---------------------------------- | ------------ | ----------------------------------------- |
| `Pageable`                         | 입력값          | 클라이언트가 요청한 pageNum, limit, sort 조건을 담은 객체 |
| `Page<T>`                          | 출력값          | 해당 조건으로 조회된 실제 데이터 + 페이지 메타정보를 담은 객체      |
| `PageRequest.of(page, size, sort)` | Pageable 구현체 | Page 설정을 생성하는 팩토리 메서드                     |
| `Page.getContent()`                | 데이터 리스트      | 실제 결과 리스트                                 |
| `Page.getTotalElements()`          | 전체 데이터 수     | countQuery 결과                             |
| `Page.getTotalPages()`             | 전체 페이지 수     | totalElements ÷ size                      |

- 
- 
- 전시 리스트 반환 양식
```json
{
  "username": "heewon",
  "exhibitions": [
    { "exhibitionId": 1, "name": "전시A", "isViewed": false },
    { "exhibitionId": 2, "name": "전시B", "isViewed": true },
    { "exhibitionId": 3, "name": "전시C", "isViewed": false }
  ],
  "pageInfo": {
    "pageNum": 1,
    "limit": 3,
    "totalPages": 2,
    "totalElements": 6
  }
}

```