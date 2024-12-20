# 선착순 쿠폰 시스템 - Consumer 프로젝트

이 프로젝트는 **선착순 쿠폰 발급 시스템**의 **Consumer** 역할을 담당합니다. 쿠폰 발급 이벤트를 **Kafka**를 통해 소비하고, 해당 이벤트를 처리하는 로직을 구현하고 있습니다. 또한, 실패한 메시지를 **Dead Letter Queue**에 전송하는 방식으로 안정적인 메시지 처리를 보장합니다.

## **프로젝트 관련 GitHub 리포지토리**

| **No** | **Repository Name** | **Description** | **URL** |
| --- | --- | --- | --- |
| 1 | CouponSystem | 선착순 쿠폰 시스템의 메인 애플리케이션 | [CouponSystem GitHub](https://github.com/kwongio/CouponSystem) |
| 2 | consumer | Kafka Consumer 관련 서비스 및 처리 로직 | [consumer GitHub](https://github.com/kwongio/consumer) |

## **프로젝트 구조**

```jsx
src
├─main
│  ├─java
│  │  └─org
│  │      └─example
│  │          └─consumer
│  │              │  ConsumerApplication.java  # 애플리케이션 진입점
│  │              │  
│  │              ├─config                   
│  │              │      ConsumerConfigLog.java  # Consumer 설정 정보 로깅을 위한 클래스
│  │              │      KafkaConfig.java        # Kafka Consumer 및 Producer 설정 클래스
│  │              │      
│  │              ├─consumer                 
│  │              │      CouponAssignRequest.java  # 쿠폰 발급 요청 DTO 클래스
│  │              │      CouponConsumer.java      # Kafka 메시지를 소비하는 Consumer 클래스
│  │              │      StringToCouponConverter.java  # 문자열을 쿠폰 객체로 변환하는 클래스
│  │              │      
│  │              ├─domain                   
│  │              │      CouponAssignLog.java  # 쿠폰 발급 로그 기록용 엔티티
│  │              │      
│  │              └─repository              
│  │                      CouponAssignLogRepository.java  # 쿠폰 발급 로그를 관리하는 리포지토리
│  └─resources
│      │  application.yml   # 애플리케이션 설정 파일
│      │  
│      ├─static
│      └─templates
└─test
    └─java
        └─org
            └─example
                └─consumer
                        ConsumerApplicationTests.java

```
