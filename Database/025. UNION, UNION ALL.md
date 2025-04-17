# UNION, UNION ALL

---

## 1. 개요

### UNION 이란

- 여러 쿼리문들을 합쳐서 하나의 쿼리문으로 만들어주는 방법이다.
- 중복된 값을 제거하고 보여준다. 
- 중복된 값을 제거하는 연산이 추가로 수행되기 때문에 UNION ALL보다 속도가 느리다.


### UNION ALL 이란

- UNION과 동일하게 여러 쿼리문들을 합쳐서 하나의 쿼리문으로 만들어주는 방법이다. 
- 중복된 값을 모두 보여준다.

### 사용형태

- 컬럼명이 동일해야 한다. (같지 않을 경우 AS 를 이용해서 동일하게 맞춰줘야 한다.)
- 컬럼별로 데이터 타입이 동일해야 한다. 
- 출력할 컬럼의 개수가 동일해야 한다. 
- 출력할 컬럼명을 차례로 적고, 알리어스(AS) 를 통해 컬럼명을 동일하게 맞춰준다.

```sql
SELECT A AS one, B AS two
FROM TABLE_A
UNION (또는 UNION ALL)
SELECT C AS one, D AS two
FROM TABLE_B;
```

### JOIN과 차이점

- JOIN : 새로운 열로 결합한다. (수평결합)
- UNION : 새로운 행으로 결합한다. (수직결합)

---

## 2. 사용 이유

### 중복 제거가 필요한 경우 (UNION)

- 예를 들어, 두 테이블 TBL_A와 TBL_B에서 사용자 정보를 조회할 때 중복된 사용자를 제거하고 싶다면 UNION을 사용한다
```sql
SELECT USER_NO, USER_NAME FROM TBL_A
UNION
SELECT USER_NO, USER_NAME FROM TBL_B;
```
- 이 쿼리는 중복된 사용자 정보를 제거한 결과를 반환

### 가상의 테이블 생성 (UNION ALL)

- 가상의 데이터셋을 만들 때는 UNION ALL을 사용하여 중복 제거 없이 데이터를 합친다.
```sql
SELECT * FROM (
SELECT 'A' AS CODE, 'A등급' AS CODE_NAME, 90 AS SCORE FROM DUAL UNION ALL
SELECT 'B' AS CODE, 'B등급' AS CODE_NAME, 80 AS SCORE FROM DUAL UNION ALL
SELECT 'C' AS CODE, 'C등급' AS CODE_NAME, 70 AS SCORE FROM DUAL UNION ALL
SELECT 'D' AS CODE, 'D등급' AS CODE_NAME, 60 AS SCORE FROM DUAL
) A;
```
- 이 쿼리는 중복을 제거하지 않고 모든 등급 데이터를 포함한 결과를 생성

---

## 3. UNION 시 컬럼 개수 및 데이터 유형이 다른 컬럼 찾기

- UNION을 사용할 때, 컬럼 개수나 데이터 타입이 다르면 오류가 발생
- 눈으로 확인하기 힘든 대용량 테이블에서 이런 문제를 쉽게 찾는 방법을 소개

### 임시 테이블 생성
   
```sql
CREATE TABLE TBL_TMP1 AS
SELECT COL1, COL2, COL3, COL4 .... COL50
FROM TBL1
WHERE 1 = 2;

CREATE TABLE TBL_TMP2 AS
SELECT COL1, COL2, COL3, COL4 .... COL50
FROM TBL2
WHERE 1 = 2;
```
- 데이터를 가져오지 않고 구조만 복사 (WHERE 1 = 2)

### 컬럼 타입 및 정보 비교 쿼리

```sql
SELECT A.TABLE_NAME, A.COLUMN_NAME, A.DATA_TYPE, A.DATA_LENGTH, A.DATA_PRECISION,
B.TABLE_NAME, B.COLUMN_NAME, B.DATA_TYPE, B.DATA_LENGTH, B.DATA_PRECISION
FROM (
    SELECT * FROM DBA_TAB_COLUMNS
    WHERE OWNER = 'TEST' AND TABLE_NAME = 'TBL1'
) A
FULL OUTER JOIN (
    SELECT * FROM DBA_TAB_COLUMNS
    WHERE OWNER = 'TEST' AND TABLE_NAME = 'TBL2'
) B
ON A.TABLE_NAME = B.TABLE_NAME
   AND A.COLUMN_NAME = B.COLUMN_NAME
   AND A.DATA_TYPE = B.DATA_TYPE;
```
- FULL OUTER JOIN을 사용하여 불일치 컬럼을 한눈에 파악.

### 정리 (임시 테이블 삭제)

```sql
DROP TABLE TBL_TMP1;
DROP TABLE TBL_TMP2;
```

- 임시 테이블을 통해 UNION 대상 컬럼 구조를 미리 비교 가능 
- DBA_TAB_COLUMNS 뷰를 활용하여 정확한 컬럼 정보(타입, 길이 등) 확인 
- 대용량 테이블이나 다수의 컬럼을 가진 쿼리에서 유용

---

### 4. UNION vs UNION ALL

| 항목                     | UNION                                           | UNION ALL                                           |
|------------------------|------------------------------------------------|----------------------------------------------------|
| 중복 제거 여부           | 중복된 행을 제거함                                  | 중복된 행도 모두 포함함                                 |
| 속도/성능               | 느림 (중복 제거 연산 수행됨)                          | 빠름 (중복 제거 없음)                                   |
| 사용 목적               | 중복 제거가 필요한 경우                               | 모든 데이터를 합쳐야 할 경우 (중복 포함)                      |
| 결합 방식               | 수직 결합 (행 추가)                                 | 수직 결합 (행 추가)                                     |
| 컬럼 수와 데이터 타입      | 동일해야 함                                        | 동일해야 함                                            |
| 실무 예시               | 사용자 중복 없이 통합 목록 만들 때                         | 가상의 코드 테이블을 만들거나 로그 데이터를 합칠 때               |
| 대표 SQL 예시           | `SELECT A FROM T1 UNION SELECT A FROM T2`       | `SELECT A FROM T1 UNION ALL SELECT A FROM T2`       |

---

### 참고자료

[UNION과 UNION ALL 1](https://silverji.tistory.com/49)

[UNION과 UNION ALL 2](https://mystyle70024.tistory.com/23)


