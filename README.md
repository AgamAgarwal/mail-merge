mail-merge
==========

Useful for sending a bunch of similar mails.

Using
-----

__list file__

Tab separated entries. Zeroth entry has to be recipient's mail id.

__subject file__
Just the subject (needs to be common, as of now)

__message file__
Contains the message to be sent. Use {i} to put in entry from index i.
Note - Currently no support for escaping messages of the form _{i}_