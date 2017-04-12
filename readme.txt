Files:

I. Input:
	- email.properties : 
		- subject: the mail's subject
		- emailBodyTemplate: the mail's body. It is a stringtemplate file to preserve text formatting. !Don't change the first and the last line
		- senderName : you can use it in the tmeplate as parameter in the format of : {senderName}
		- recipientSource: the name of the file what is contains the recipinets list. Thre is one email address / line
		There is an another template parameter : {recipientName} . It is calculeted from the recipient email address by cutting the @domain part from the addtress.
		pl: JohnDoo@gmail.com -> johnDoo

	- email_account.properties : 
		- email: your gmail address
		- username, password: the user and password of your gmail

	- emailBody.st: the template file of the email body. You can edit it.

	- recipients.txt: the recipients email address

II. Output:
	- MailSender.log: All information logs by the application



Important: If you want use your gmail account to send mails, you have to switch on "Allow less secure apps" temporaly. Link : https://myaccount.google.com/lesssecureapps