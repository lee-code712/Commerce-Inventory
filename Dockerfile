FROM gradle:7.1.0-jdk11

USER root

RUN useradd -ms /bin/bash wasadm

RUN rm -rf /app
RUN mkdir -p /app

RUN chown -R wasadm:wasadm /app

# RUN git clone https://github.com/lee-code712/agent.java.git /app/agent.java
RUN git clone https://github.com/Song-Hyun-Jung/jenniferAgent.git /app/jenniferAgent
RUN git clone https://github.com/lee-code712/Commerce-Common.git /app/Commerce-Common
COPY . /app/Commerce-Inventory

WORKDIR /app/Commerce-Inventory

RUN cd /app/Commerce-Inventory
RUN gradle build

CMD ["java", "-Djennifer.config=/app/jenniferAgent/conf/inventory.conf","-javaagent:/app/jenniferAgent/jennifer.jar", "-jar", "/app/Commerce-Inventory/build/libs/Commerce-Inventory-0.0.1-SNAPSHOT.jar"]