Customising Support For Encodings Which Don't Need A Separate Input Method
--------------------------------------------------------------------------

For supporting for languages and encodings, Sanchay uses the following
properties files:

- languages.txt: List of languages and their ISO codes
- encodings.txt: List of encodings and their Sanchay codes
- lang-encs.txt: Possible associations between languages and encodings
- lang-enc-font-props.txt: The paths of fonts for various language-encoding
  pairs (works on subdirectories)
- lang-enc-default-fonts.txt: Names (not file names) of default fonts for various language-encoding pairs

To add support for a new encoding for a given language, you have to do the
following:

1. Make a directory in the Sanchay/fonts/<language directory> for the new
encoding

2. Put the fonts for that encoding in this new directroy

3. Make appropriate entries in the files mentioned above.

After you do this, the encodings list (e.g., in Sanchay Editor) should show
the encoding as a choice for your language and when you have selected that
encoding, the font chooser should list the fonts applicable only for that
langauge-encoding pairs (these will be fonts you put in the directory for the
new encoding).

NOTE: We have tried to include only those fonts here which are freely available for use and can be redistributed. If you find that any of the fonts doesn't fall in this category, please let us know so that we can remove it from the Sanchay distribution. If you are a user and want to continue to use such fonts, please ensure that you have the license to use them.
