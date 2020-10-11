[bintray]: https://bintray.com/azzerial/maven/jmgur/_latestVersion
[bintray_svg]: https://api.bintray.com/packages/azzerial/maven/jmgur/images/download.svg
[license]: https://github.com/Azzerial/Jmgur/tree/master/LICENSE
[license_svg]: https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg
[discord]: <https://discord.com/invite/APF2ZGD>
[discord_svg]: <https://discordapp.com/api/guilds/757580090547634206/widget.png>
[![bintray_svg][]][bintray]
[![license_svg][]][license]
[![discord_svg][]][discord]

## Jmgur (Java Imgur API)

Jmgur provides the most complete wrapping of the Imgur API for Java.

This library requires **Java 8+** and is compatible with Android projects development.



1. [Usage](#usage)
2. [Repositories and Endpoints](#repositories-and-endpoints)
3. [RestAction and PagedRestAction](#restAction-and-pagedRestAction)
4. [Releases](#releases)
5. [License](#license)



## Usage

The whole API holds into a single class called **Jmgur**. You can get the current instance of the Jmgur object in any object returned by the API (through the `getApi()` method of that object).

The Jmgur object is build via the **JmgurBuilder** class. When building it, you have to set the **client id** of your Imgur application, the OAuth2 object and can as well set other options via the provided setters. The Jmgur object is then created by calling the `JmgurBuilder#build` method.

The **OAuth2** object holds the connected user's data returned by Imgur after the OAuth2 authorization.

**Example:**
```java
public static void main(String[] args) {
    // There are 2 ways of building the OAuth2 object.
    OAuth2 oauth;
    
    // 1. Via Url (when the authorization response type is set to 'token')
    oauth = OAuth2.fromUrl("authorization_callback_url");
    
    // 2. Via Raw Data
    oauth = OAuth2.fromData(
        "access_token",
        "refresh_token",
        0, // expires_in
        "account_username",
        0 // account_id
    );
    
    // Creating the Jmgur object
    Jmgur api = JmgurBuilder
        .of("client_id")
        .setOAuth(oauth)
        .build();

    /* do something */

    // Shutdown the API webclient and threadpools
    api.shutdown();
}
```

<details>
<summary>Show Android example</summary>
<p>

```java
public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "client_id";
    private static final String TAG = "JMGUR_APP_TAG";
    
    public static Jmgur api = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        Log.i(TAG, "Opening to OAuth2 login");
    
        final FrameLayout root = new FrameLayout(this);
        final WebView webView = new WebView(this);
        final String oauthUrl = "https://api.imgur.com/oauth2/authorize?client_id=" + CLIENT_ID + "&response_type=token";
    
        root.addView(webView);
        setContentView(root);
    
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final String url = request.getUrl().toString();
    
                Log.i(TAG, "Redirected WebView on: " + url);
    
                try {
                    final OAuth2 oauth = OAuth2.fromUrl(url);
    
                    api = JmgurBuilder
                        .of(CLIENT_ID)
                        .setOAuth(oauth)
                        .build();
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                    return false;
                }
    
                api.ACCOUNT.getSelfAccount().queue(
                    account -> Log.i(TAG, "Logged in account: " + account),
                    Throwable::printStackTrace
                );
                return true;
            }
        });
    
        webView.loadUrl(oauthUrl);
    
        Log.i(TAG, "Opened WebView on: " + oauthUrl);
    }
}
\```

</p>
</details>

Once your Jmgur object is built, you can start requesting data to the Imgur API.

​```java
public static void main(String[] args) {
    Jmgur api = JmgurBuilder
        ...
        .build();

    // Get and print jmgur_guy's account data
    api.ACCOUNT.getUserAccount("jmgur_guy").queue(
        System.out::println,
        Throwable::printStackTrace
    );
    
    // Get and print jmgur_guy's gallery favorites (order from oldest to newest)
    api.ACCOUNT.getUserGalleryFavorites("jmgur_guy", FavoriteSort.OLDEST)
        .get()
        .queue(
            System.out::println,
            Throwable::printStackTrace
        );
    
    api.shutdown();
}
```



## Repositories and Endpoints

The API endpoints are split into several repositories:

| Repository | Endpoints actions |
| ---------- | --------- |
| **Account** | get users accounts<br />get users avatars and available avatars<br />block/unblock users, get blocked users list<br />get users favorites and submissions<br />get/update self account settings<br />get/delete users albums, comments or images |
| **Comment** | get/create/delete a comment<br />vote for/report a comment |
| **Album** | get/create/delete an album<br />edit an album content<br />favorite an album |
| **Gallery** | get the gallery latest submissions<br />search for albums/images in the gallery<br />share/unshare an album/image to the gallery<br />report a gallery<br />get/post votes/comments on a gallery |
| **Image** | get/create/delete an image<br />edit an image information<br />favorite an image |

In Jmgur, these repositories are directly available through the Jmgur object as follows:

```java
Jmgur api = ...;

api.ACCOUNT // Account repository
api.COMMENT // Comment repository        NOT AVAILABLE YET
api.ALBUM   // Album repository          NOT AVAILABLE YET
api.GALLERY // Gallery repository        NOT AVAILABLE YET
api.IMAGE   // Image repository          NOT AVAILABLE YET
```



## RestAction and PagedRestAction

The **RestAction** class originally comes from the [JDA](<https://github.com/DV8FromTheWorld/JDA>) project. In Jmgur, we used parts of their code and created an other class called the **PagedRestAction** which handled paged requests.



#### RestAction\<T>

From [JDA](<https://github.com/DV8FromTheWorld/JDA>):

> If you understand RestAction you understand JDA.
>
> The `RestAction` is a step between specifying what the user wants to do and executing it, it allows the user to specify *how* JDA should deal with their `Request`.
>
> However this only works if you actually tell the `RestAction` to do *something*. That is why we recommend checking out whether or not something in JDA returns a `RestAction`. If that is the case you **have** to execute it using one of the `RestAction` execution operations:
>
> * [queue()](https://github.com/DV8FromTheWorld/JDA/wiki/7%29-Using-RestAction#using-queue), [queue(Consumer)](https://github.com/DV8FromTheWorld/JDA/wiki/7%29-Using-RestAction#using-queue), [queue(Consumer, Consumer)](https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/requests/RestAction.html#queue(java.util.function.Consumer,java.util.function.Consumer))
>   These operations are **asynchronous** and will not execute within the same Thread.
>   This means that you cannot use procedural logic when you use `queue()`, unless you use the callback Consumers. Only similar requests are internally executed in sequence such as sending messages in the same channel or adding reactions to the same message.
> * [submit()](https://github.com/DV8FromTheWorld/JDA/wiki/7%29-Using-RestAction#using-submit)
>   Provides request future to cancel tasks later and avoid callback hell.
> * [complete()](https://github.com/DV8FromTheWorld/JDA/wiki/7%29-Using-RestAction#using-complete)
>   This operation will **block** the current Thread until the request has been finished and will return the response type.
>
> **Note**: We recommend using `queue()` or `submit()` when possible as blocking the current Thread can cause downtime and will use more resources.

**Examples:**

###### queue(), queue(Consumer), queue(Consumer, Consumer)

```java
Jmgur api = ...;

// To execute a request
api.ACCOUNT.blockUser("jmgur_guy").queue();

// With consumers
api.ACCOUNT.getSelfAccountSettings().queue(
    settings -> System.out.printf("The logged in user's email is: %s%n", settings.getEmail()),
    error -> {
        System.err.println("An error occurred.");
        error.printStackTrace();
    }
);
```

###### submit()

```java
Jmgur api = ...;

// Useful when chaining requests
api.ACCOUNT.getSelfAvailableAvatars().submit()
    .thenCompose(avatars -> {
        final AccountSettingsDTO settings = AccountSettingsDTO.create()
            .setAvatar(avatars.get(0).getName());
        return api.ACCOUNT.updateSelfAccountSettings(settings).submit();
    }).thenAccept(success ->
        System.out.printf("%s the account's settings%n",
            success ? "Successfully updated" : "Failed to update"
        )
    );
```

###### complete()

```java
Jmgur api = ...;

// Directly get the reponse type
Account account = api.ACCOUNT.getSelfAccount().complete();
```

For more information, please check the original documentation page on [JDA's wiki](<https://github.com/DV8FromTheWorld/JDA/wiki/7%29-Using-RestAction>)



#### PagedRestAction\<T>

In short, the PagedRestAction is an intermediate between some methods and the RestAction.

For some of Imgur API endpoints, it is needed for the developer to specify a page to request information from. We made the PagedRestAction class in order to help the developers using Jmgur to handle paged requests. The PagedRestAction contains a few handy tools to iterate over the pages.

###### get(), get(int)

Get the current page of the iterator or request get a specific page.

###### next()

Get the current page and increment the iterator to the next page.

###### page()

Get the iterator value.

###### reset()

Reset the iterator.

###### skip(int), skipTo(int)

Jump some pages. Increment the iterator by a specific amount or set it to that amount.

**Examples:**

```java
Jmgur api = ...;

// get()
api.ACCOUNT.getSelfGalleryFavorites()
    .get(); // returns the RestAction of the 1st page

// get(int)
api.ACCOUNT.getSelfSubmissions()
    .get(12); // returns the RestAction of the 12th page

// get(), reset, skip(), skipTo(int)
api.ACCOUNT.getUserAlbums("jmgur_guy")
    .skip(2) // skisp 2 pages (now on the 3rd page)
    .skipTo(7) // skips to the 7th page (now on the 7th page)
    .reset() // resets iterator (now on the 1st page)
    .get(); // returns the RestAction of the 1st page

// next(), page()
PagedRestAction<List<Comment>> pages = api.ACCOUNT.getSelfComments(CommentSort.BEST);
while (pages.page() != 10) // loops from the 1st page to the 10th page (0 to 9)
    pages.next(); // returns the RestAction of the xth page (1st page to 10th page)
```



## Releases

Latest Version: [![bintray_svg][]][bintray]

#### Gradle

> Be sure to replace the **VERSION** key with the latest version shown above.

```groovy
dependencies {
    compile 'net.azzerial:jmgur:VERSION'
}
```

```groovy
repositories {
    jcenter()
}
```
#### Maven

> Be sure to replace the **VERSION** key with the latest version shown above.

```xml
<dependency>
    <groupId>net.azzerial</groupId>
    <artifactId>jmgur</artifactId>
    <version>VERSION</version>
</dependency>
```

```xml
<repository>
    <id>jcenter</id>
    <name>jcenter-bintray</name>
    <url>https://jcenter.bintray.com</url>
</repository>
```



## License

```
Copyright 2020 Robin Mercier

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

