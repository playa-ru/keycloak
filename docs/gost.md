# Библиотека российских алгоритмов шифрования для Keycloak

Поддержка российских алгоритмов шифрования для подписания токенов JWT в [Keycloak](https://www.keycloak.org/)

При использовании OPENID для подписи информации, содержащейся в токенах доступа стандартно используется RSA. Наша 
сборка позволяет использовать для подписи этой информации алгоритмы ГОСТ, реализованные в сертифицированной по 
российским стандартам библиотеке [CryptoPro JCP ](https://www.cryptopro.ru/products/csp/jcp)

В [Keycloak](https://www.keycloak.org/) добавлена совместимость со следующими алгоритмами шифрования:
+ ГОСТ Р 34.10-2012 (512) - **GOST256**
+ ГОСТ Р 34.10-2012 (256) - **GOST512**

### Запуск Keycloak
Запуск [Keycloak](https://www.keycloak.org/) необходимо осуществлять на Java с установленным JCP.

### Совместимость
+ [Keycloak](https://www.keycloak.org/) - 4.8.3.Final
+ [JCP](https://www.cryptopro.ru/products/csp/jcp) - 2.0

### Настройка KeyStore
Для настройки KeyStore переходим в раздел **Providers** (*Real* &#8658; *Realm Settings* &#8658; *Keys* &#8658; 
*Providers*). В списке **Add keystore** выбираем **gost-keystore**.
![Создания приложение ВКонтакте](gost_2.png)

В открывшемся оке заполняем поля:
1. Заполняем поле *Название* - **Console Display Name**.
2. Заполняем поле *Приоритет провайдера* - **Priority**.
3. Выбираем из списка *Алгоритм шифрования* - **Algorithm**.
4. Заполняем поле *Название хранилищя ключей и сертификатов* - **Keystore**.
5. Заполняем поле *Пароль от контейнера* - **Keystore Password**. 
6. Заполняем поле *Название контейнера* - **Key Alias**.
7. Нажимаем кнопку **Save**.
![Создания приложение ВКонтакте](gost_3.png)

### Выбор алгоритма подписи токена.
Для выбора алгоритма подписи токена переходим в раздел **Tokens** (*Real* &#8658; *Realm Settings* &#8658; *Tokens*). 
В списке **Default Signature Algorithm** выбераем алгоритм подписи токена. Нажимаем кнопку **Save**.
![Создания приложение ВКонтакте](gost_4.png)

### Запуск
Существует два способа запуска:
1. Wildfly
2. Spring-Boot

### Wildfly
Необходимо заменить файл *keycloak-core-4.8.3.Final.jar* находящийся в директории 
`${WILDFLY_HOME}\modules\system\add-ons\keycloak\org\keycloak\keycloak-core\main` на файл 
`kecloak-core.4.8.3.Final.GOST.jar` который можно скачать [здесь](https://nexus.playa.ru/nexus/repository/releases/org/keycloak/keycloak-core/4.8.3.Final.GOST/keycloak-core-4.8.3.Final.GOST.jar).

### Spring-Boot

1. *Exclude* стандартной библиотеки **keycloak-core**: 
```
<dependencies>
  <dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-2-adapter</artifactId>
    <exclusions>
      <exclusion>
        <groupId>org.keycloak</groupId>
        <artifactId>keycloak-core</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
  <dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-starter</artifactId>
    <exclusions>
      <exclusion>
        <groupId>org.keycloak</groupId>
        <artifactId>keycloak-core</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
</dependencies>
```
2. Подключить библиотеку поддерживающую российские алгоритмы шифрования.
```
<dependencies>
  <dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-core</artifactId>
    <version>4.8.3.Final.GOST</version>
  </dependency>
  <dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-common</artifactId>
    <version>4.8.3.Final.GOST</version>
  </dependency>
</dependencies>
```