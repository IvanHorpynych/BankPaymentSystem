"# BankPaymentSystem" 

How to launch and configure system for start working:

1.Clone from GitHub;

2.Create user in db environment : login: root; password: root;

3.Launch script bank_system.sql

4.For deploying with Maven you need create in Tomcat (tomcat-users.xml):
    (<role rolename="manager-script"/>
    <user username="deployer" password="root" roles="manager-script"/>)
    
5.Create configuration in settings.xml:
    (<servers>
        <server>
            <id>TomcatServer</id>
            <username>deployer</username>
            <password>root</password>
        </server>
    </servers>)

6.Execute in Maven:mvn tomcat7:deploy;

7.Credentials for user: test@test.com, qwerty 
 Credentials for manager: test@email.com, qwerty 

8.Enjoy =)