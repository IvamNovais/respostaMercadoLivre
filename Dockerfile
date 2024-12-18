# Imagem base com Java JDK 21
FROM openjdk:21-jdk-slim

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar todos os arquivos do projeto para o contêiner
COPY . /app

# Ajustar permissões do Maven Wrapper
RUN chmod +x ./mvnw

# Executar o build do projeto
RUN ./mvnw clean install

# Expor a porta configurada na aplicação
EXPOSE 9000

# Comando para rodar a aplicação
CMD ["java", "-jar", "target/resposta-0.0.1-SNAPSHOT.jar"]
