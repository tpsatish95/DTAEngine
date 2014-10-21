package com.diskoverorta.entities;

import com.diskoverorta.osdep.StanfordNLP;
import com.diskoverorta.vo.EntityObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 17/10/14.
 */
public class EntityManager
{
    //to do : Get all selected entities at runtime and process only them, Make use of array of baseentities
    static StanfordNLP nlpStanford = null;
    public EntityManager()
    {
        if(nlpStanford==null)
        {
            nlpStanford = new StanfordNLP();
        }
    }

    public static void main(String args[])
    {
        String str = "India’s indigenously developed nuclear capable sub-sonic cruise missile ‘Nirbhay’, which can strike targets more than 700 km away, was on Friday test-fired from a test range at Chandipur in Odisha.“The missile was test-fired from a mobile launcher positioned at launch pad 3 of the Integrated Test Range at about 10.03 hours,” said an official soon after the flight took off from the launch ground.“Flight details will be available after data retrieved from radars and telemetry points, monitoring the trajectories, are analysed,” the official said. It is the second test of the sub-sonic long range cruise missile ‘Nirbhay’ from the ITR. The maiden flight, conducted on March 12, 2013 could not achieve all the desired parameters as “the flight had to be terminated mid-way when deviations were observed from its intended course,” sources said. India has in its arsenal the 290-km range supersonic “BrahMos” cruise missile which is jointly developed by India and Russia. But ‘Nirbhay’ with long range capability is a different kind of missile being developed by the Defence Research and Development Organisation (DRDO).";
        EntityManager temp = new EntityManager();
        System.out.println(temp.getALLDocumentEntitiesINJSON(str));
    }

    public String getALLDocumentEntitiesINJSON(String sDoc)
    {
        List<EntityObject> allEntities = getALLEntitiesForDocument(sDoc);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(allEntities);
        return jsonOutput;
    }

    List<EntityObject> getALLEntitiesForDocument(String sDoc)
    {
        List<EntityObject> allEntities = new ArrayList<EntityObject>();
        List<String> sentences = nlpStanford.splitSentencesINDocument(sDoc);
        for(String temp : sentences)
        {
            allEntities.add(getALLEntitiesForSentence(temp));
        }
        return allEntities;
    }

    EntityObject getALLEntitiesForSentence(String sSentence)
    {
        EntityObject entities = new EntityObject();
        List<List<CoreLabel>> entity7Tags = nlpStanford.get7NERTaggedOutput(sSentence);
        List<List<CoreLabel>> entity3Tags = nlpStanford.get3NERTaggedOutput(sSentence);

        entities.sentence = sSentence;
        entities.person = (new PersonEntity()).getEntities(entity3Tags);
        entities.organization = (new OrganizationEntity()).getEntities(entity3Tags);
        entities.location = (new LocationEntity()).getEntities(entity3Tags);
        entities.date = (new DateEntity()).getEntities(entity7Tags);
        entities.time = (new TimeEntity()).getEntities(entity7Tags);
        entities.currency = (new CurrencyEntity()).getEntities(entity7Tags);
        entities.percent = (new PercentEntity()).getEntities(entity7Tags);

        return entities;
    }
}
