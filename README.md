spring boot 프로젝트를 위한 템플릿 입니다.

업데이트 기준일 : 2022년 9월 18일

# 프로젝트 구성
## java
java 17을 적용하였습니다.

## spring boot
`2.7.3` 버전이 적용되었습니다.

## Testing
### junit 5
junit 5를 사용하여 단위 테스트를 작성합니다.

`org.junit.jupiter:junit-jupiter-api:5.9.0` 가 적용되었습니다. 

### assertj
단위 테스트 작성시에 assert 작성은 assertj를 사용하여 작성합니다.
`org.assertj:assertj-core:3.23.1` 가 적용되었습니다.

### Database 
로컬 테스팅 환경에서는 h2를 사용하여 테스트를 수행합니다.
