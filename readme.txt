Files:

I. Input:
	- email.properties : 
		- subject: the subject of the mail
		- emailBodyTemplate: the body of the mail. It is a stringtemplate file. To preserve text formatting, !Don't change the first and the last line.
		- senderName : you can use it in the tmeplate as a parameter in the format of : {senderName}
		- recipientSource: the name of the file that contains the recipinets list. There is one recipient name and email address / line (separeted by tab)
		- alreadySentRecipientsSource: the name of the file what contains the list of the already sent email addresses. these mails won't be send again.
		- badEmailAddresses: list of the mail addresses that dropped exception during the sending process.
		- packageSize: how many mail will be send in one packege.
		There is an another template parameter : {recipientName} . 

	- email_account.properties : 
		- email: your gmail address
		- username, password: the user and password of your gmail

	- emailBody.st: the template file of the email body. You can edit it.

	- recipients.txt: the recipients email address

II. Output:
	- MailSender.log: All information logs by the application



!Important: If you want to use your gmail account to send mails, you have to switch on "Allow less secure apps" temporaly. Link : https://myaccount.google.com/lesssecureapps
