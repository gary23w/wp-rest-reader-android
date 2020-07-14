package com.gary.news.view.adapters


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gary.news.R
import com.gary.news.model.Topics
import com.gary.news.utils.loadImage
import com.gary.news.view.WebView
import kotlinx.android.synthetic.main.item_topic.view.*


class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicItemViewHolder>() {

    val topicItems = arrayListOf<Topics>()

    fun onAddNewsItem(item: Topics?) {
        topicItems.add(Topics("COVID-19", "https://the-latest.news/wp-content/uploads/2020/02/3779/gilead-sciences-drug-may-help-treat-coronavirus-symptoms-according-to-who.jpg", "1"))
        topicItems.add(Topics("News", "https://the-latest.news/wp-content/uploads/2020/06/60634/four-men-charged-in-attack-on-jackson-statue-near-white-house-yaktrinews-com.jpg", "1"))
        topicItems.add(Topics("Entertainment", "https://the-latest.news/wp-content/uploads/2020/06/60307/how-one-teaspoon-of-amazon-soil-teems-with-fungal-life.jpg", "1"))
        topicItems.add(Topics("Sports", "https://the-latest.news/wp-content/uploads/2020/06/53967/fans-wants-sports-sports-want-fans-but-its-not-that-simple.jpg", "1"))
        topicItems.add(Topics("UK News", "https://the-latest.news/wp-content/uploads/2020/06/56010/authorities-in-uk-will-investigate-english-park-stabbing-as-a-terrorist-incident.jpg", "1"))
        topicItems.add(Topics("Canada News", "https://the-latest.news/wp-content/uploads/2020/06/61303/canada-is-going-to-be-building-canadarm3-for-the-artemis-missions.jpg", "1"))
        topicItems.add(Topics("Technology", "https://the-latest.news/wp-content/uploads/2020/06/56453/new-processing-technology-for-maximizing-energy-densities-of-high-capacity-lithium-ion-batteries.jpg", "1"))
        topicItems.add(Topics("World News", "https://the-latest.news/wp-content/uploads/2020/06/59284/research-suggests-that-trees-absorb-less-carbon-dioxide-as-the-worlds-temperature-rises.jpg", "1"))
        topicItems.add(Topics("Science", "https://the-latest.news/wp-content/uploads/2020/06/61147/stocks-making-the-biggest-moves-in-the-premarket-gilead-sciences-starbucks-bp-coty-more.jpeg", "1"))
        topicItems.add(Topics("Business", "https://the-latest.news/wp-content/uploads/2020/06/54959/treasury-to-release-names-of-big-businesses-that-received-ppp-loans.jpg", "1"))
        topicItems.add(Topics("Weather", "https://the-latest.news/wp-content/uploads/2020/05/27564/record-temperatures-and-dry-weather-have-sparked-more-than-a-dozen-wildfires-in-florida.jpg", "1"))
        topicItems.add(Topics("Deals", "https://the-latest.news/wp-content/uploads/2020/06/44418/6-tech-deals-you-can-get-for-extra-cheap-right-now.jpg", "1"))
        topicItems.add(Topics("Web", "https://the-latest.news/wp-content/uploads/2020/07/62361/senator-loefflers-new-section-230-reform-bill-would-threaten-encryption-and-pressure-websites-to-keep-spam-porn.png", "1"))
        topicItems.add(Topics("Smartphones", "https://the-latest.news/wp-content/uploads/2020/04/20391/the-best-5g-smartphones-you-can-buy-today.jpg", "1"))
        topicItems.add(Topics("Funny", "https://the-latest.news/wp-content/uploads/2020/05/40086/netflixs-space-force-set-in-colorado-is-too-impolitical-for-the-times-and-frankly-not-funny.jpg", "1"))
        topicItems.add(Topics("NFL", "https://the-latest.news/wp-content/uploads/2020/06/55549/nflpa-medical-director-advises-players-to-halt-group-workouts-due-to-some-states-covid-spikes.jpg", "1"))
        topicItems.add(Topics("NHL", "https://the-latest.news/wp-content/uploads/2020/06/59071/its-complicated-but-phase-1-of-nhl-draft-lottery-takes-place-friday.jpg", "1"))
        topicItems.add(Topics("Buzz", "https://the-latest.news/wp-content/uploads/2020/05/26959/scientists-buzzing-over-virgin-birth-and-genetic-mystery-thats-been-solved.jpg", "1"))
        topicItems.add(Topics("Women", "https://the-latest.news/wp-content/uploads/2020/06/58897/after-twitter-outcry-five-women-detail-chris-delias-alleged-sexual-improprieties.jpg", "1"))
        topicItems.add(Topics("Health", "https://the-latest.news/wp-content/uploads/2020/06/56631/south-korea-battling-second-wave-of-coronavirus-health-official.jpg", "1"))
        topicItems.add(Topics("Politics", "https://the-latest.news/wp-content/uploads/2020/05/41069/vaccine-politics-take-center-stage-in-competitive-democratic-primaries.jpeg", "1"))
        topicItems.add(Topics("Lifestyle", "https://the-latest.news/wp-content/uploads/2020/05/28918/tekashi-6ix9ine-with-4m-net-worth-has-200k-donation-declined-by-charity-over-controversial-lifestyle.jpeg", "1"))
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) =
        TopicItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        )
    override fun getItemCount() = topicItems.size
    override fun onBindViewHolder(holder: TopicItemViewHolder, position: Int) {
        holder.bind(topicItems[position])
    }
    class TopicItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.topicTitle
        private val image = view.blipPic

        fun bind(topicItem: Topics) {
            title.text = topicItem.name
            image.loadImage(topicItem.imagelink)
            itemView.setOnClickListener {
                val context= itemView.context
                val intent = Intent( context, WebView::class.java)
                intent.putExtra("urlSearch", topicItem.name)
                context.startActivity(intent)
            }
        }
    }

}

