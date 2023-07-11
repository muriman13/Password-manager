<h1  align="center">Password manager</h1>



The application I have developed resembles the most common password manager with
the difference is that it eliminates one of the biggest weaknesses of most similar
applications, single point of failure. Instead of all passwords being stored in one
db, which would mean that if someone gets access to the db it's the end for everyones
passwords. What I propose as a solution is the following. Similar
of all other password managers, before saving the password I encrypt it
using a master key that only the user knows and is not stored anywhere. On this one
way even we have no idea what user passwords are. The difference compared to
other password managers is that instead of storing the encrypted password on a db or on one cluster of db's, I break the encrypted password in two and store both
halves of two independent, unrelated databases which adds one more level of
security.

you can read more here - https://docs.google.com/document/d/1ymoTT8zJqKZYvzcr5vkAVciq_D3otG28/edit?usp=sharing&ouid=103609850713108469419&rtpof=true&sd=true
 
![image](https://github.com/muriman13/Password-manager/assets/100300225/3222d7a5-d285-41fe-babf-ad0b8d44a90b)

