#mail-merge


Useful for sending a bunch of similar mails.

##Usage
Type the following to use mail-merge:

```bash
java MailMerge username subject_file message_file list_file
```
The various command line arguments are below:

###list file
Tab separated entries. Zeroth entry has to be recipient's mail id.

###subject file
Just the subject (needs to be common, as of now)

###message file
Contains the message to be sent. Use `{i}` to put in entry from index i.

Note - For printing text in the form `{i}` use `\` to escape the ending brace.
For example, to print `{1}`, type `{1\}` in the message file.
