package TubeSlice.tubeSlice.domain.postKeyword;


import TubeSlice.tubeSlice.domain.keyword.Keyword;
import TubeSlice.tubeSlice.domain.keyword.KeywordRepository;
import TubeSlice.tubeSlice.domain.post.Post;
import TubeSlice.tubeSlice.domain.post.dto.request.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostKeywordService {

    private final PostKeywordRepository postKeywordRepository;
    private final KeywordRepository keywordRepository;

    public void savePostKeyword(PostRequestDto.PostCreateDto postRequestDto, Post post) {
        for (String pk: postRequestDto.getPostKeywords()){
            Optional<Keyword> findKeyword = keywordRepository.findByName(pk);
            if (findKeyword.isEmpty()) {
                Keyword keyword = Keyword.builder()
                        .name(pk)
                        .build();
                keywordRepository.save(keyword);
                PostKeyword postKeyword = PostKeyword.builder()
                        .keyword(keyword)
                        .post(post)
                        .build();
                postKeywordRepository.save(postKeyword);
            }else { //keyword 테이블에 이미 있는 키워드 저장.
                PostKeyword postKeyword = PostKeyword.builder()
                        .keyword(findKeyword.get())
                        .post(post)
                        .build();
                postKeywordRepository.save(postKeyword);
            }
        }
    }

    public void updatePostKeyword(Long postId, List<String> updatePostKeywords, Post findPost) {
        for (String pk : updatePostKeywords) {
            Optional<Keyword> findKeyword = keywordRepository.findByName(pk);
            Keyword keyword;
            PostKeyword findPostKeyword = postKeywordRepository.findByKeywordAndPostId(findKeyword.orElse(null), postId);

            if (findKeyword.isEmpty()){ //keyword 테이블에 없으면 keyword랑 post_keyword에 둘다 저장 필요.
                keyword = Keyword.builder()
                        .name(pk)
                        .build();
                keywordRepository.save(keyword);
                PostKeyword postKeyword = PostKeyword.builder()
                        .keyword(keyword)
                        .post(findPost)
                        .build();

                postKeywordRepository.save(postKeyword);
            } else if (findPostKeyword == null){    //keyword에는 있는데 post_keyword에 없는 경우.

                PostKeyword postKeyword = PostKeyword.builder()
                        .keyword(findKeyword.get())
                        .post(findPost)
                        .build();

                postKeywordRepository.save(postKeyword);
            }
        }

        //기존 postKeyword 와 비교해서 삭제된 keyword를 db에서 삭제.
        List<PostKeyword> findPostKeywords = postKeywordRepository.findByPostId(postId);
        for (PostKeyword postKeyword: findPostKeywords){
            if (!updatePostKeywords.contains(postKeyword.getKeyword().getName())){
                postKeywordRepository.delete(postKeyword);
            }
        }
    }


}
