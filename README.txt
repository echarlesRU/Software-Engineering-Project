Web Chase: "The Expert Web Crawler"
ITEC 370: Software Engineering 1
Professor: Dr. Tracey Lewis-Williams
Radford University Fall Semester 2016

The members of this project team are:

- John Fiipowicz
- Mitchell Powell
- Christopher Shimp
- Josh Ervin
- Evan Charles 

Client: Dr. Joe Chase of Radford University


Introduction

  This application is a small GUI built over a web crawler. The crawler allows
multiple starting URL's and their children web pages of a specified depth to
be searched for specific strings. A string may be a specific word (ex. "Dog")
or it may be a phrase (ex. "The man in black fled across the desert, and the
gunslinger followed."). The crawler will search for an exact match, whether it
is just the single word or a whole sentance. Output will be given from the
results found, and will be savable.

  The three peices to a search or query are the initial URLs, the search
depth, and the search terms/strings. While there is no limit on the search
depth (per request of the client), it is recommended to keep in mind that the
growth in search time is not linear with the growth of depth, it is
exponential.

  The returned output may be saved as a file with any of the wanted results.
Wanted results may be marked on the output page, or specified on the save pop
up. Past search results will be available to be opened within the application
for later reference as well.



dist.zip contains the primary jar file "WebChase.jar" and all needed libraries to run. See user manual for more information.
