# ShowcaseView

<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/4048813/14598878/fc7ed3ac-0571-11e6-94e4-6d98f9ceffe2.png" width="250"/>
  <img src="https://cloud.githubusercontent.com/assets/4048813/14600231/306fc39a-0579-11e6-9b0d-35c6788bb361.png" width="250"/>
  <img src="https://cloud.githubusercontent.com/assets/4048813/14598879/fca1afd0-0571-11e6-9d9a-f49fb2dd7ea1.png" width="250"/>
</p>

This ShowcaseView library can be used to showcase any specific part of the UI or can even be used during OnBoarding of a user to give a short intro about different widgets visible on the screen. You may add any number of views (ImageView, TextView, FrameLayout, etc displaying images, videos, GIF, text etc) to describe the showcasing view.

<p>Try out sample application on <a href="https://play.google.com/store/apps/details?id=showcaseview.outlander.showcaseviewdemo">Android PlayStore</a></p>

# Usage

<i>For a working implementation of this project see the /app folder </i>
<ol>
  <li>
    <p>Include the library as local library project or add this dependency in your build.gradle.</p>
    <pre>
      <code>
        dependencies {
          compile 'com.outlander.showcaseview:showcaseview:1.3.0'
        }
      </code>
    </pre>
  </li>
  <li>
    <p>Initialise the ShowcaseViewBuilder as follows:</p>
      <pre>
      <code>
        ShowcaseViewBuilder showcaseViewBuilder;
        ...
        showcaseViewBuilder = ShowcaseViewBuilder.init(this)
                .setTargetView(fab)
                .setBackgroundOverlayColor(0xdd4d4d4d)
                .setRingColor(0xcc8e8e8e)
                .setRingWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(R.drawable.arrow_up), Gravity.LEFT)
                .addCustomView(R.layout.description_view, Gravity.TOP)
                .addCustomView(R.layout.skip_layout)
                .setCustomViewMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()));
        
        showcaseViewBuilder.show();
      </code>
    </pre>
  </li>
  <li>
    <p>Register click listeners on the customView added as follows:</p>
        <pre>
        <code>
          showcaseViewBuilder.setClickListenerOnView(R.id.exit_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add thing to do when clicked.
            }
          });
        </code>
      </pre>
  </li>
  <li>
    <p>To hide the showcaseView just call:</p>
        <pre>
        <code>
          showcaseViewBuilder.hide()
        </code>
      </pre>
  </li>
  <li>
    <p>To hide the showcaseView when user clicks on the overlay, just set the following flag: <code>showcaseViewBuilder.setHideOnTouchOutside(true);</code> By default, this is always false.</p>
  </li>
</ol>

#Things to keep in mind
<ul>
  <li>
    <p>Call the <code>showcaseViewBuilder.show()</code> only after adding all the customViews and MarkerDrawable.</p>
  </li>
  <li>
      <p><code>setRingWidth(float width) and other margin setters take pixels as parameters. So make sure to send into density independent pixels (dp) value to support multiple screen sizes (See the sample code snippet above for reference)</p>
    </li>
  <li>
    <p>Once <code>showcaseViewBuilder.hide()</code> is called, all the click listeners get <b>deregistered</b>.
    Thus, you will have to set them back if showing it again. Better to register all the click listeners in a single method which can be called when showing the showcaseView.</p>
  </li>
</ul>

#Method Definitions
<ul>
  <li>
    <p><code>setTargetView(View v)</code>: Sets the view which needs to be showcased.</p>
  </li>
  <li>
    <p><code>setBackgroundOverlayColor(int color)</code>: Sets the color of the overlay to be shown.</p>
  </li>
  <li>
    <p><code>setRingColor(int color)</code>: Sets the color of the ring around the showcaseView.</p>
  </li>
  <li>
    <p><code>setRingWidth(float width)</code>: Sets the width of the ring around the showcaseView. Default value is 10px</p>
  </li>
  <li>
    <p><code>setMarkerDrawable(Drawable drawable, int gravity)</code>: Sets the marker drawable if any to point the showcaseView. Also, sets a gravity for the drawable (TOP, LEFT, RIGHT, BOTTOM) around the showcasing view.</p>
  </li>
  <li>
    <p><code>setDrawableLeftMargin(float margin)</code>: Sets the marker drawable left margin.</p>
  </li>
  <li>
    <p><code>setDrawableTopMargin(float margin)</code>: Sets the marker drawable top margin.</p>
  </li>
  <li>
    <p><code>addCustomView(View view, int gravity)</code>: Sets the custom description view to describe the showcaseView. Also, sets a gravity for the view (TOP, LEFT, RIGHT, BOTTOM) around the showcasing view.</p>
  </li>
  <li>
    <p><code>addCustomView(View view)</code>: Sets the custom description view to describe the showcaseView. This doesn't takes any gravity as an argument and renders the view as per the gravity defined in the layout file.</p>
  </li>
  <li>
    <p><code>setCustomViewMargin(int margin)</code>: Sets the custom description view margin from the showcaseView in the direction of the gravity defined, if any. If no gravity defined, then no point in using this method.</p>
  </li>
  <li>
    <p><code>setHideOnTouchOutside(boolean hide)</code>: Sets the flag which decide whether to hide the showcase overlay when user touches on the screen anywhere.</p>
  </li>
  <li>
    <p><code>setClickListenerOnView(int id, View.OnClickListener clickListener)</code>: Sets clicklistener on the components of the customView(s) added.</p>
  </li>
  <li>
    <p><code>show()</code>: Start showcasing the targetView.</p>
  </li>
  <li>
    <p><code>hide()</code>: Stop showcasing the targetView.</p>
  </li>
</ul>

#Developed by
<ul>
  <li>
    <a href="https://github.com/outlander24">Aashish Totla</a>
  </li>
</ul>

#License

    Copyright Aashish Totla © 2016. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
