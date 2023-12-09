# Medcare

## Descrição do Projeto
<p> Esse projeto foi criado para o trabalho de conclusão de Curso da minha pós-graduação em Engenharia de Software.
Consiste em uma API REST de um sistema médico. </p>

## Tecnologias Utilizadas
* java 17
* docker
* postgres
* springboot
* JPA e Hibernate

## Como rodar Localmente
Para rodar localmente essa aplicação é necessário ter o docker desktop instalado localmente na máquina
após clonar o repositório, deverá entrar no diretório raiz do projeto e criar um arquivo .env com o conteudo:
MEDCARE_USER={usuario-do-banco}
MEDCARE_PASSWORD={senha-do-banco}                        
Esse arquivo externo é para não armazenar dados sensíveis na aplicação e o docker-compose irá pegar essas variáveis de ambiente através desse arquivo.
Após isso, é só rodar docker-compose up -d que irá subir o conteiner com o banco postgres e o conteiner com a aplicação. 
A estrutura do banco é montada via código pelo JPA assim que a aplicação sobe.


