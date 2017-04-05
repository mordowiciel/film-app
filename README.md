# film-app
Tutorial app project from Udacity using film database.

ATTENTION: Please note that I'm still completing the Udacity course, so the app may be refactored in many ways. I'm still learning Android framework, so the solutions presented here are not final.

TODO/Refactor list:

- Using ASyncTask for fetching data in background is not really recommended. Use services?
- Fetching data from server is done in really "vanilla" way. Switch to Retrofit library?
- Add some settings to the Settings activity.
- Add dynamic search button to the Main Activity to allow user searching for any movie/tv show.
- Improve MovieDetails activity - add viewing YouTube trailer, parsing date string, exceptional poster "No Poster Available" (when poster path returned by db is null)?
