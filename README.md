# 🧾 Order Service

주문 생성/조회/취소/삭제를 담당하는 마이크로서비스입니다.

---

## 📌 서비스 정보

- **Service name**: `order-service` (`spring.application.name`)
- **Port**: `8086` (`project-configs/configs/order-service/order-service.yml`)
- **External API base path**: `/api/v1/orders`
- **Internal API base path**: `/internal/v1/orders` (서비스 간 통신 전용, Gateway 라우팅 제외)

---

## 🏗️ API 엔드포인트 요약

### External (클라이언트 → Gateway → order-service)

- `POST /api/v1/orders` 주문 생성
- `GET /api/v1/orders` 주문 목록 조회 (페이징)
- `GET /api/v1/orders/{orderId}` 주문 단건 조회
- `PATCH /api/v1/orders/{orderId}` 주문 취소
- `DELETE /api/v1/orders/{orderId}` 주문 삭제(소프트/권한 정책에 따름)

### Internal (서비스 ↔ 서비스)

- `GET /internal/v1/orders/{orderId}` 내부용 주문 조회

---

## 🔐 인증/헤더 규칙 (Gateway 사용 시)

Gateway는 JWT 검증 후 아래 헤더를 하위 서비스로 전달합니다.

- `X-User-Id`: 사용자 UUID (Keycloak `sub`)
- `X-User-Email`: 사용자 이메일
- `X-User-Role`: 사용자 권한 (예: `ADMIN`, `HUB_ADMIN`, `DELIVERY`, `COMPANY`)

클라이언트(Postman)는 Gateway로 요청할 때 보통 아래만 넣으면 됩니다.

```http
Authorization: Bearer {JWT_TOKEN}
```

> `order-service` 컨트롤러는 `X-User-Id`/`X-User-Role` 헤더를 사용합니다.  
> (Gateway가 자동 주입. order-service를 직접 호출하는 경우에는 Postman에서 직접 넣어야 합니다.)

---

## 🧪 Postman 테스트 (복붙용)

### 1) Gateway로 호출 (추천)

**Base URL**

```text
http://localhost:8080
```

#### 주문 생성

```text
POST http://localhost:8080/api/v1/orders
```

Headers:

```text
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

Body:

```json
{
  "supplierCompanyId": "11111111-1111-1111-1111-111111111111",
  "receiverCompanyId": "22222222-2222-2222-2222-222222222222",
  "deadlineAt": "2026-04-06T12:34:56",
  "requestMessage": "주문 테스트",
  "orderItems": [
    { "productId": "33333333-3333-3333-3333-333333333333", "quantity": 2 }
  ]
}
```

#### 주문 목록 조회

```text
GET http://localhost:8080/api/v1/orders?page=0&size=10&sort=createdAt,desc
```

Headers:

```text
Authorization: Bearer <JWT_TOKEN>
```

---

### 2) order-service 직접 호출 (개발/디버깅용)

**Base URL**

```text
http://localhost:8086
```

#### 주문 생성

```text
POST http://localhost:8086/api/v1/orders
```

Headers:

```text
Content-Type: application/json
X-User-Id: <USER_UUID>
```

#### 주문 목록/단건 조회

```text
GET http://localhost:8086/api/v1/orders
GET http://localhost:8086/api/v1/orders/<ORDER_ID_UUID>
```

Headers:

```text
X-User-Id: <USER_UUID>
X-User-Role: COMPANY
```

> `X-User-Role` 가능한 값: `ADMIN | HUB_ADMIN | DELIVERY | COMPANY`


---

## 🧩 의존 서비스

주문 생성 플로우에서 외부 서비스 호출이 포함되어 있습니다(Feign).

- `hub-service`: 재고 차감/복구
- `delivery-service`: 배송 생성
- `notification-service`: 슬랙 메시지 생성
- `company-service`: 상품명 조회

> 위 서비스들이 내려가 있으면 주문 생성 시 실패할 수 있습니다.

