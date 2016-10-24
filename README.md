# Project 2 - *MyTimes*

**Name of your app** is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: **30** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* [x] User can click on "settings" which allows selection of **advanced search options** to filter results
* [x] User can configure advanced search filters such as:
  * [x] Begin Date (using a date picker)
  * [x] News desk values (Arts, Fashion & Style, Sports)
  * [x] Sort order (oldest or newest)
* [x] Subsequent searches have any filters applied to the search results
* [x] User can tap on any article in results to view the contents in an embedded browser.
* [x] User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* [x] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [x] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [x] User can **share an article link** to their friends or email it to themselves
* [x] Replaced Filter Settings Activity with a lightweight modal overlay
* [x] Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [x] Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [x] For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [x] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [x] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [x] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [x] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* [x] Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [x] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.
* [x] Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing. (_**2 points**_)

The following **additional** features are implemented:

* [x] Added home page to see popular articles
* [x] Articles is home page are horizontally scrollable using RecyclerView
* [x] Making top article api call to get popular politics, national, sports and fashion articles
* [x] Replaced actionbar with toolbar
* [x] Added search by voice feature
* [x] Added feature to hide toolbar when the user scrolls down
* [x] Added Snackbar with a retry buton when there is no internet connection
* [x] Making use of SharedPreferences to persist data
* [x] User can pull-to-refresh
* [x] Added a slight delay during endless scrolling to avoid getting API rate limiting error

## Video Walkthrough

Here's a walkthrough of implemented user stories:

[YouTube](https://www.youtube.com/watch?v=jf1cqx_t_uQ)
[sendvid](http://4.sendvid.com/f4uy4m9z.mp4)

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [OkHttp](https://github.com/square/okhttp) - An HTTP & HTTP/2 client for Android and Java applications.
- [Retrofit](https://github.com/square/retrofit) - Type-safe HTTP client for Android and Java by Square, Inc.
- [MaterialDateRangePicker](https://github.com/borax12/MaterialDateRangePicker) - A material Date Range Picker based on wdullaers MaterialDateTimePicker
- [Material Dialogs](https://github.com/afollestad/material-dialogs) - A beautiful, fluid, and customizable dialogs API.
- [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling.
- [Parceler](https://github.com/johncarl81/parceler) - Android Parcelables made easy through code generation.
- [gson](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back.

## License

    Copyright [2016] [Sandeep Raveesh]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
